package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.enums.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DandelionRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    private Member member1;

    private Dandelion dandelion1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dandelionRepository.deleteAll();
        em.flush();
        em.clear();

        member1 = Member.builder()
                .id("아이디1")
                .password("패스워드1")
                .tel("010-0000-0001")
                .build();

        dandelion1 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();

    }

    @Test
    @DisplayName("민들레 엔티티 조회 - 데이터가 존재하는 경우")
    void findDandelionSuccessExist() {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        Dandelion findDandelion = dandelionRepository.findBySeq(savedDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion).isNotNull();
        assertThat(findDandelion.getBlossomedDate()).isEqualTo(dandelion1.getBlossomedDate());
        assertThat(findDandelion.getCommunity()).isEqualTo(dandelion1.getCommunity());
        assertThat(findDandelion.getFlowerSignNumber()).isEqualTo(dandelion1.getFlowerSignNumber());
        assertThat(findDandelion.getMember().getSeq()).isEqualTo(dandelion1.getMember().getSeq());
    }

    @Test
    @DisplayName("민들레 엔티티 조회 - 데이터가 없는 경우")
    void findDandelionSuccessNonExist() {
        // given

        // when
        Dandelion findDandelion = dandelionRepository.findBySeq(0L)
                .orElse(null);

        // then
        assertThat(findDandelion).isNull();
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - 사용중인 민들레가 0개인 경우")
    void countUsingSeedWhenNothing() {
        // given
        member1.getDandelions().clear();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - flying 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenFlying() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - flying 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenFlyingAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        dandelion1.delete();
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - hold 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenHold() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }
}