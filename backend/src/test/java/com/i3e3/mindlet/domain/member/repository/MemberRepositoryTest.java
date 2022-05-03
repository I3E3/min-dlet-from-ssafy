package com.i3e3.mindlet.domain.member.repository;

import com.i3e3.mindlet.domain.member.entity.Member;
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
class MemberRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    private Member member1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        em.flush();
        em.clear();

        member1 = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .tel("010-0000-0001")
                .build();
    }

    @Test
    @DisplayName("회원 데이터 유무 확인 - 데이터가 있는 경우")
    void checkMemberExistSuccess() {
        // given
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        boolean isExist = memberRepository.existsBySeq(savedMember.getSeq());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("회원 데이터 유무 확인 - 데이터가 없는 경우")
    void checkMemberNoneExist() {
        // given

        // when
        boolean isExist = memberRepository.existsBySeq(0L);

        // then
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("회원 데이터 유무 확인 - 데이터가 있지만 탈퇴한 경우")
    void checkMemberExistWhenDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        savedMember.delete();
        em.flush();
        em.clear();

        // when
        boolean isExist = memberRepository.existsBySeq(savedMember.getSeq());

        // then
        assertThat(isExist).isFalse();
    }
}
