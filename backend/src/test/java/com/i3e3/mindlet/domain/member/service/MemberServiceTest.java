package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.domain.member.controller.dto.LoginRequestDto;
import com.i3e3.mindlet.domain.member.controller.dto.RegisterRequestDto;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.domain.member.service.dto.response.MemberInfoDto;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member1;

    private AppConfig appConfig1;

    private RegisterRequestDto registerRequestDto1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        appConfigRepository.deleteAll();

        em.flush();
        em.clear();

        member1 = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .build();

        appConfig1 = AppConfig.builder()
                .member(member1)
                .language(AppConfig.Language.ENGLISH)
                .build();

        registerRequestDto1 = RegisterRequestDto.builder()
                .id("id01")
                .password("pass12#$")
                .build();
    }

    @Test
    @DisplayName("회원 식별키로 커뮤니티 변경 - 성공")
    void changeCommunitySuccess() {

        //given
        Member savedMember = memberRepository.save(member1);
        appConfig1.changeCommunity(Community.WORLD);
        appConfigRepository.save(appConfig1);

        em.flush();
        em.clear();

        //when
        memberService.changeCommunity(savedMember.getSeq(), Community.KOREA);
        AppConfig changedAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(changedAppConfig.getCommunity()).isEqualTo(Community.KOREA);
        assertThat(changedAppConfig.getMember().getSeq()).isEqualTo(savedMember.getSeq());
    }

    @Test
    @DisplayName("회원 식별키로 커뮤니티 변경 - 회원 정보가 없을 때")
    void changeCommunityFailNonMember() {
        //given

        //when

        //then
        assertThatThrownBy(() -> memberService.changeCommunity(0L, Community.KOREA))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 커뮤니티 변경 - 회원 정보가 있는데 deleted 가 true")
    void changeCommunityFailExistMemberAndDeleted() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);
        savedMember.delete();
        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> memberService.changeCommunity(savedMember.getSeq(), Community.KOREA))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 성공 - 음소거 on")
    void changeSoundSuccessMuteOn() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);

        em.flush();
        em.clear();

        //when
        memberService.changeSound(savedMember.getSeq(), true);
        AppConfig changedAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(changedAppConfig.isSoundOff()).isTrue();
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 성공 - 음소거 Off")
    void changeSoundSuccessMuteOff() {
        //given
        Member savedMember = memberRepository.save(member1);
        AppConfig savedConfig = appConfigRepository.save(appConfig1);
        savedConfig.soundOff();

        em.flush();
        em.clear();

        //when
        memberService.changeSound(savedMember.getSeq(), false);
        AppConfig changedAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(changedAppConfig.isSoundOff()).isFalse();
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 실패 - 회원 정보가 없을 때")
    void changeSoundFailNonMember() {
        //given

        //when

        //then
        assertThatThrownBy(() -> memberService.changeSound(0L, true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
        assertThatThrownBy(() -> memberService.changeSound(0L, false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 사운드 변경 실패 - 회원 정보가 있는데 deleted 가 true")
    void changeSoundFailExistMemberAndDeleted() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);
        savedMember.delete();
        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> memberService.changeSound(savedMember.getSeq(), true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
        assertThatThrownBy(() -> memberService.changeSound(savedMember.getSeq(), false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("아이디 중복 확인 - 중복된 경우 : 회원 데이터 있음")
    void checkIdDuplicateTrue() {
        // given
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        boolean isExist = memberService.isExistsId(savedMember.getId());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("아이디 중복 확인 - 중복된 경우 : 회원 데이터 있음 및 회원 탈퇴")
    void checkIdDuplicateTrueWhenDeletedMember() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        boolean isExist = memberService.isExistsId(savedMember.getId());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("아이디 중복 확인 - 중복이 아닌 경우 : 회원 데이터 없음")
    void checkIdDuplicateFalseNotExistMember() {
        // given

        // when
        boolean isExist = memberService.isExistsId("no1234");

        // then
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void registerSuccess() {
        // given

        // when
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());

        // then
        Member findMember = memberRepository.findBySeq(savedMember.getSeq())
                .orElse(null);
        assertThat(findMember.getId()).isEqualTo(registerRequestDto1.getId());
        assertThat(passwordEncoder.matches(registerRequestDto1.getPassword(), findMember.getPassword())).isTrue();

        AppConfig findAppConfig = findMember.getAppConfig();
        assertThat(findAppConfig.getLanguage()).isEqualTo(AppConfig.Language.KOREAN);
        assertThat(findAppConfig.getCommunity()).isEqualTo(Community.KOREA);
        assertThat(findAppConfig.getMember().getSeq()).isEqualTo(savedMember.getSeq());
    }

    @Test
    @DisplayName("로그인 - 성공")
    void loginSuccess() {
        // given
        memberService.register(registerRequestDto1.toServiceDto());
        LoginRequestDto newLoginRequestDto = LoginRequestDto.builder()
                .id(registerRequestDto1.getId())
                .password(registerRequestDto1.getPassword())
                .build();

        // when
        boolean isLogin = memberService.login(newLoginRequestDto.toServiceDto());

        // then
        assertThat(isLogin).isTrue();
    }

    @Test
    @DisplayName("로그인 - 실패 : 회원 데이터가 없는 경우")
    void loginFailWhenNotExistMember() {
        // given
        LoginRequestDto newLoginRequestDto = LoginRequestDto.builder()
                .id(registerRequestDto1.getId())
                .password(registerRequestDto1.getPassword())
                .build();

        // when
        boolean isLogin = memberService.login(newLoginRequestDto.toServiceDto());

        // then
        assertThat(isLogin).isFalse();
    }

    @Test
    @DisplayName("로그인 - 실패 : 패스워드를 잘못 입력한 경우")
    void loginFailWhenInvalidPassword() {
        // given
        memberService.register(registerRequestDto1.toServiceDto());
        LoginRequestDto newLoginRequestDto = LoginRequestDto.builder()
                .id(registerRequestDto1.getId())
                .password(registerRequestDto1.getPassword() + "invalid")
                .build();

        // when
        boolean isLogin = memberService.login(newLoginRequestDto.toServiceDto());

        // then
        assertThat(isLogin).isFalse();
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 - 데이터가 있는 경우")
    void findMemberInfoById() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());

        // when
        MemberInfoDto findMemberInfo = memberService.getMemberInfoById(savedMember.getId());

        // then
        assertThat(findMemberInfo.getSeq()).isEqualTo(savedMember.getSeq());
        assertThat(findMemberInfo.getCommunity()).isEqualTo(savedMember.getAppConfig().getCommunity());
        assertThat(findMemberInfo.getLanguage()).isEqualTo(savedMember.getAppConfig().getLanguage());
        assertThat(findMemberInfo.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 - 데이터가 있지만 삭제 처리된 경우")
    void findMemberInfoByIdWHenDeleted() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());
        savedMember.delete();
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> memberService.getMemberInfoById(savedMember.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_ID.getMessage());
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 - 회원 데이터가 없는 경우")
    void findMemberInfoByIdWhenNotExistMember() {
        // given


        // when

        // then
        assertThatThrownBy(() -> memberService.getMemberInfoById("id01"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_ID.getMessage());
    }

    @Test
    @DisplayName("언어 변경 - 성공")
    void changeLanguageSuccess() {

        //given
        Member savedMember = memberRepository.save(member1);
        AppConfig savedAppConfig = appConfigRepository.save(appConfig1);
        savedAppConfig.changeLanguage(AppConfig.Language.ENGLISH);
        em.flush();
        em.clear();

        //when
        memberService.changeLanguage(savedMember.getSeq(), AppConfig.Language.KOREAN);
        AppConfig findAppConfig = appConfigRepository.findByMemberSeq(savedMember.getSeq())
                .orElse(null);

        //then
        assertThat(findAppConfig.getLanguage()).isEqualTo(AppConfig.Language.KOREAN);
    }

    @Test
    @DisplayName("언어 변경 - 실패 / 회원이 null")
    void changeLanguageFailNotExistMember() {
        //given

        //when

        //then
        assertThatThrownBy(() -> memberService.changeLanguage(0L, AppConfig.Language.KOREAN))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("언어 변경 - 실패 / 회원이 deleted")
    void changeLanguageFailExistMemberDeleted() {
        //given
        Member savedMember = memberRepository.save(member1);
        appConfigRepository.save(appConfig1);
        savedMember.delete();

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> memberService.changeLanguage(savedMember.getSeq(), AppConfig.Language.KOREAN))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("회원 식별키로 회원 정보 조회 - 데이터가 있는 경우")
    void findMemberInfoBySeq() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());

        // when
        MemberInfoDto findMemberInfo = memberService.getMemberInfoBySeq(savedMember.getSeq());

        // then
        assertThat(findMemberInfo.getSeq()).isEqualTo(savedMember.getSeq());
        assertThat(findMemberInfo.getCommunity()).isEqualTo(savedMember.getAppConfig().getCommunity());
        assertThat(findMemberInfo.getLanguage()).isEqualTo(savedMember.getAppConfig().getLanguage());
        assertThat(findMemberInfo.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("회원 식별키로 회원 정보 조회 - 데이터가 있지만 삭제 처리된 경우")
    void findMemberInfoBySeqWHenDeleted() {
        // given
        Member savedMember = memberService.register(registerRequestDto1.toServiceDto());
        savedMember.delete();
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> memberService.getMemberInfoBySeq(savedMember.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}