package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


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
}