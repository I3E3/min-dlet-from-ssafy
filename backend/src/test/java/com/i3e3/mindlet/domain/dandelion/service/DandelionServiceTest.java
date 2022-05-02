package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.member.entity.Member;
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
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("민들레 상태(Blossomed) 확인 - 예외 발생")
    void checkBlossomedException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isBlossomed(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 주인 확인 - 주인이 맞는 경우")
    void checkOwnerTrue() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        boolean isOwner = dandelionService.isOwner(savedDandelion1.getSeq(), savedMember1.getSeq());

        // then
        assertThat(isOwner).isTrue();
    }

    @Test
    @DisplayName("민들레 주인 확인 - 주인이 아닌 경우")
    void checkOwnerFalse() {
        // given
        memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Member savedMember2 = memberRepository.save(member2);
        dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        boolean isOwner = dandelionService.isOwner(savedDandelion1.getSeq(), savedMember2.getSeq());

        // then
        assertThat(isOwner).isFalse();
    }

    @Test
    @DisplayName("민들레 주인 확인 - 예외 발생")
    void checkOwnerException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isOwner(0L, 0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 꽃말 수정 - 성공")
    void changeDescriptionSuccess() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        String newDescription = savedDandelion1.getDescription() + "1";

        // when
        String findDescription = dandelionService.changeDescription(savedDandelion1.getSeq(), newDescription);

        // then
        assertThat(findDescription).isEqualTo(newDescription);
        assertThat(findDescription).isNotEqualTo(savedDandelion1.getDescription());
    }

    @Test
    @DisplayName("민들레 꽃말 수정 - 예외 발생")
    void changeDescriptionException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.changeDescription(0L, "꽃말"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}