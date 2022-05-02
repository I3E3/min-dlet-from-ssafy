package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
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
class DandelionServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private DandelionService dandelionService;

    private Member member1, member2;

    private Dandelion dandelion1, dandelion2;

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

        member2 = Member.builder()
                .id("아이디2")
                .password("패스워드2")
                .tel("010-0000-0002")
                .build();

        dandelion1 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();

        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member2)
                .build();
    }

    @Test
    @DisplayName("민들레 상태(Blossomed) 확인 - True")
    void checkBlossomedTrue() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.BLOSSOMED);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isBlossomed = dandelionService.isBlossomed(savedDandelion.getSeq());

        // then
        assertThat(isBlossomed).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Blossomed) 확인 - False")
    void checkBlossomedFalse() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isBlossomed = dandelionService.isBlossomed(savedDandelion.getSeq());

        // then
        assertThat(isBlossomed).isFalse();
    }
}