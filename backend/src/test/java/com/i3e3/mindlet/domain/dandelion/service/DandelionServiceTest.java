package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.service.dto.SeedCountDto;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.dandelion.DandelionConst;
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
                .build();

        member2 = Member.builder()
                .id("아이디2")
                .password("패스워드2")
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

    @Test
    @DisplayName("남은 씨앗 개수 조회 - 예외 발생: 해당 회원이 존재하지 않는 경우")
    void countLeftSeedWhenNotExistMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getLeftSeedCount(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - 민들레가 1개도 등록되지 않은 경우")
    void countLeftSeedWhenNotExistDandelion() {
        // given
        member1.getDandelions().clear();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - flying 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenFlying() {
        // given
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - flying 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenFlyingAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.FLYING);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - hold 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenHold() {
        // given
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - hold 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenHoldAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.HOLD);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - return 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenReturn() {
        // given
        dandelion1.changeStatus(Dandelion.Status.RETURN);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - return 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenReturnAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.RETURN);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - pending 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenPending() {
        // given
        dandelion1.changeStatus(Dandelion.Status.PENDING);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - pending 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenPendingAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.PENDING);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - blossomed 상태의 민들레가 1개 등록될 경우")
    void countLeftSeedWhenBlossomed() {
        // given
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue() - 1);
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - blossomed 상태의 민들레가 1개 있지만 삭제된 경우")
    void countLeftSeedWhenBlossomedAndDeleted() {
        // given
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        dandelion1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - blocked 상태의 민들레가 1개인 경우")
    void countLeftSeedWhenBlocked() {
        // given
        dandelion1.changeStatus(Dandelion.Status.BLOCKED);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - album 상태의 민들레가 1개인 경우")
    void countLeftSeedWhenAlbum() {
        // given
        dandelion1.changeStatus(Dandelion.Status.ALBUM);
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        SeedCountDto seedCountDto = dandelionService.getLeftSeedCount(savedMember.getSeq());

        // then
        assertThat(seedCountDto.getLeftSeedCount()).isEqualTo(DandelionConst.MAX_USING_DANDELION_COUNT.getValue());
    }

    @Test
    @DisplayName("남은 씨앗 개수 조회 - 한 회원이 6개의 민들레를 등록한 경우")
    void countLeftSeedWhenMoreThanMaxCount() {
        // given
        Member savedMember = memberRepository.save(Member.builder()
                .id("test1")
                .password("midlet")
                .build());

        for (int i = 1; i <= 6; i++) {
            dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-05-03"))
                    .community(Community.WORLD)
                    .flowerSignNumber(i)
                    .member(savedMember)
                    .build());
        }
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getLeftSeedCount(savedMember.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.MORE_THAN_MAX_COUNT.getMessage());
    }

    @Test
    @DisplayName("민들레 상태(Return) 확인 - True")
    void checkReturnTrue() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-04"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.RETURN);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isReturn = dandelionService.isReturn(savedDandelion.getSeq());

        // then
        assertThat(isReturn).isTrue();
    }

    @Test
    @DisplayName("민들레 상태(Return) 확인 - False")
    void checkReturnFalse() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-04"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.FLYING);
        Dandelion savedDandelion = dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        boolean isReturn = dandelionService.isReturn(savedDandelion.getSeq());

        // then
        assertThat(isReturn).isFalse();
    }

    @Test
    @DisplayName("민들레 상태(Return) 확인 - 예외 발생")
    void checkReturnException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.isReturn(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 상태 변경")
    void changeDandelionStatus() {
        // given
        memberRepository.save(member1);
        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-04"))
                .community(Community.WORLD)
                .flowerSignNumber(1)
                .member(member1)
                .build();
        newDandelion.changeStatus(Dandelion.Status.RETURN);
        dandelionRepository.save(newDandelion);
        em.flush();
        em.clear();

        // when
        dandelionService.changeStatus(newDandelion.getSeq(), Dandelion.Status.BLOSSOMED);
        Dandelion findDandelion = dandelionRepository.findBySeq(newDandelion.getSeq())
                .orElse(null);

        // then
        assertThat(findDandelion.getStatus()).isEqualTo(Dandelion.Status.BLOSSOMED);
    }

    @Test
    @DisplayName("민들레 상태 변경 - 예외 발생")
    void changeDandelionStatusException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.changeStatus(0L, Dandelion.Status.FLYING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }
}