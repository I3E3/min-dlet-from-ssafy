package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    private Member member1;

    private AppConfig appConfig1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        appConfigRepository.deleteAll();

        em.flush();
        em.clear();

        member1 = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .tel("010-1234-1234")
                .build();

        appConfig1 = AppConfig.builder()
                .member(member1)
                .language(AppConfig.Language.ENGLISH)
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
}