package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.repository.PetalRepository;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.dandelion.service.dto.DandelionSeedDto;
import com.i3e3.mindlet.domain.dandelion.service.dto.ResponseGardenInfoDto;
import com.i3e3.mindlet.domain.dandelion.service.dto.SeedCountDto;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import com.i3e3.mindlet.domain.member.repository.MemberDandelionHistoryRepository;
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
import java.util.List;

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

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PetalRepository petalRepository;

    @Autowired
    private MemberDandelionHistoryRepository memberDandelionHistoryRepository;

    private Member member1, member2, member3;

    private AppConfig appConfig1, appConfig2, appConfig3;

    private Dandelion dandelion1, dandelion2, dandelion3;

    private Tag tag1;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dandelionRepository.deleteAll();
        tagRepository.deleteAll();

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

        member3 = Member.builder()
                .id("아이디3")
                .password("패스워드3")
                .build();

        appConfig1 = AppConfig.builder()
                .member(member1)
                .language(AppConfig.Language.ENGLISH)
                .build();

        appConfig2 = AppConfig.builder()
                .member(member2)
                .language(AppConfig.Language.ENGLISH)
                .build();

        appConfig3 = AppConfig.builder()
                .member(member3)
                .language(AppConfig.Language.ENGLISH)
                .build();

        dandelion1 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member1.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member1)
                .build();

        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member2)
                .build();

        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(member3.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member3)
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

    @Test
    @DisplayName("민들레 태그 삭제 성공")
    void deleteDandelionTagSuccess() {
        // given
        memberRepository.save(member1);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        tag1 = Tag.builder()
                .dandelion(savedDandelion)
                .name("2022년 팬 미팅")
                .member(savedMember3)
                .build();

        Tag savedTag = tagRepository.save(tag1);

        em.flush();
        em.clear();

        // when
        dandelionService.deleteTag(savedTag.getSeq(), member3.getSeq());

        // then
        assertThat(tagRepository.findBySeq(savedTag.getSeq())).isEmpty();
    }

    @Test
    @DisplayName("민들레 태그 삭제 실패 - Tag 없음")
    void deleteDandelionTagFailNonTag() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.deleteTag(0L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 태그 삭제 실패 - Seq 불 일치")
    void deleteDandelionTagFailNotEqualsMemberSeq() {
        // given
        memberRepository.save(member1);
        Member savedMember3 = memberRepository.save(member3);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion = dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        tag1 = Tag.builder()
                .dandelion(savedDandelion)
                .name("2022년 팬 미팅")
                .member(savedMember3)
                .build();

        Tag savedTag = tagRepository.save(tag1);

        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.deleteTag(savedTag.getSeq(), savedMember2.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 삭제 성공")
    void deleteDandelionSuccess() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        Petal savedPetal1 = petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("안녕하세요!!!")
                        .nation("Korea")
                        .city("SEOUL")
                        .nationalFlagImagePath("/static/files/images/national-flag/korea.png")
                        .build());

        em.flush();
        em.clear();


        Petal savedPetal2 = petalRepository.save(
                Petal.builder()
                        .member(savedMember2)
                        .dandelion(savedDandelion1)
                        .message("Nice to see ya")
                        .nation("CANADA")
                        .city("OTTAWA")
                        .nationalFlagImagePath("/static/files/images/national-flag/canada.png")
                        .build());

        em.flush();
        em.clear();

        // when
        dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember1.getSeq());

        // then
        assertThat(dandelionRepository.findBySeq(dandelion1.getSeq())).isEmpty();
        assertThat(petalRepository.findBySeq(savedPetal1.getSeq())).isEmpty();
        assertThat(petalRepository.findBySeq(savedPetal2.getSeq())).isEmpty();
    }

    @Test
    @DisplayName("민들레 삭제 실패 - 민들레 없음")
    void deleteDandelionFailNotExistDandelion() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        // when

        // then
        assertThatThrownBy(() -> dandelionService.deleteDandelion(0L, savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 삭제 실패 - 회원 식별키와 불 일치")
    void deleteDandelionFailNotEqualMemberSeq() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);

        petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("안녕하세요!!!")
                        .nation("Korea")
                        .city("SEOUL")
                        .nationalFlagImagePath("/static/files/images/national-flag/korea.png")
                        .build());

        em.flush();
        em.clear();

        // when


        // then
        assertThatThrownBy(() -> dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember2.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("민들레 삭제 실패 - 회원이 deleted")
    void deleteDandelionFailMemberDeleted() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        savedMember1.delete();
        petalRepository.save(
                Petal.builder()
                        .member(savedMember1)
                        .dandelion(savedDandelion1)
                        .message("안녕하세요!!!")
                        .nation("Korea")
                        .city("SEOUL")
                        .nationalFlagImagePath("/static/files/images/national-flag/korea.png")
                        .build());

        em.flush();
        em.clear();

        // when


        // then
        assertThatThrownBy(() -> dandelionService.deleteDandelion(savedDandelion1.getSeq(), savedMember1.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 성공")
    void getGardenInfoListSuccess() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(3)
                .member(member1)
                .build();
        dandelion2.changeStatus(Dandelion.Status.BLOSSOMED);
        dandelion3.changeStatus(Dandelion.Status.RETURN);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());

        // then
        assertThat(responseGardenInfos.size()).isEqualTo(3);
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(1).getStatus()).isEqualTo("BLOSSOMED");
        assertThat(responseGardenInfos.get(2).getStatus()).isEqualTo("RETURN");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(responseGardenInfos.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
        assertThat(responseGardenInfos.get(2).getSeq()).isEqualTo(savedDandelion3.getSeq());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 없는 회원 시도")
    void getGardenInfoListNoneMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getGardenInfoList(0L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 삭제된 회원 시도")
    void getGardenInfoListMemberIsDeleted() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        em.flush();
        em.clear();

        // when

        // then
        assertThatThrownBy(() -> dandelionService.getGardenInfoList(savedMember.getSeq()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorMessage.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - 삭제된 민들레 시도")
    void getGardenInfoListContainIsDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(3)
                .member(member1)
                .build();
        dandelion2.delete();
        dandelion3.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());
        // then
        assertThat(responseGardenInfos.size()).isEqualTo(1);
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - blocked 상태의 민들레 시도")
    void getGardenInfoListContainBlocked() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(3)
                .member(member1)
                .build();
        dandelion3.changeStatus(Dandelion.Status.BLOCKED);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());

        // then
        assertThat(responseGardenInfos.size()).isEqualTo(2);
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(1).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(responseGardenInfos.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 정보 반환 - album 상태의 민들레 시도")
    void getGardenInfoListContainAlbum() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(3)
                .member(member1)
                .build();
        dandelion3.changeStatus(Dandelion.Status.ALBUM);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<ResponseGardenInfoDto> responseGardenInfos = dandelionService.getGardenInfoList(savedMember.getSeq());

        // then
        assertThat(responseGardenInfos.size()).isEqualTo(2);
        assertThat(responseGardenInfos.get(0).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(1).getStatus()).isEqualTo("FLYING");
        assertThat(responseGardenInfos.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(responseGardenInfos.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("랜덤 민들레씨 정보 조회 - 성공")
    void findRandomDandelionSeed() {
        // given
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        dandelionRepository.save(dandelion2);
        dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        Petal savedPetal1 = petalRepository.save(Petal.builder()
                .message("메시지1")
                .imagePath("이미지1")
                .nation("국가1")
                .city("도시1")
                .nationalFlagImagePath("국가이미지1")
                .dandelion(savedDandelion1)
                .member(savedMember1)
                .build());
        Petal savedPetal2 = petalRepository.save(Petal.builder()
                .message("메시지2")
                .imagePath("이미지2")
                .nation("국가2")
                .city("도시2")
                .nationalFlagImagePath("국가이미지2")
                .dandelion(savedDandelion1)
                .member(savedMember2)
                .build());
        em.flush();
        em.clear();

        // when
        DandelionSeedDto dandelionSeedDto = dandelionService.getDandelionSeedDto(savedMember3.getSeq());
        Long findDandelionSeq = dandelionSeedDto.getSeq();
        Dandelion findDandelion = dandelionRepository.findBySeq(findDandelionSeq)
                .orElse(null);
        MemberDandelionHistory findMemberDandelionHistory = memberDandelionHistoryRepository.findByMemberAndDandelion(savedMember3, savedDandelion1)
                .orElse(null);

        // then

        /**
         * DTO 민들레 식별키 검증
         */
        assertThat(dandelionSeedDto.getSeq()).isEqualTo(savedDandelion1.getSeq());

        /**
         * 꽃잎 데이터 검증
         */
        List<DandelionSeedDto.PetalInfo> petalInfos = dandelionSeedDto.getPetalInfos();
        assertThat(petalInfos.size()).isEqualTo(2);

        DandelionSeedDto.PetalInfo findPetal1 = petalInfos.get(0);
        assertThat(findPetal1.getSeq()).isEqualTo(savedPetal1.getSeq());
        assertThat(findPetal1.getMessage()).isEqualTo(savedPetal1.getMessage());
        assertThat(findPetal1.getCity()).isEqualTo(savedPetal1.getCity());
        assertThat(findPetal1.getNation()).isEqualTo(savedPetal1.getNation());
        assertThat(findPetal1.getImageUrlPath()).isEqualTo(savedPetal1.getImagePath());

        DandelionSeedDto.PetalInfo findPetal2 = petalInfos.get(1);
        assertThat(findPetal2.getSeq()).isEqualTo(savedPetal2.getSeq());
        assertThat(findPetal2.getMessage()).isEqualTo(savedPetal2.getMessage());
        assertThat(findPetal2.getCity()).isEqualTo(savedPetal2.getCity());
        assertThat(findPetal2.getNation()).isEqualTo(savedPetal2.getNation());
        assertThat(findPetal2.getImageUrlPath()).isEqualTo(savedPetal2.getImagePath());

        /**
         * 민들레 데이터 검증
         */
        assertThat(findDandelion.getStatus()).isEqualTo(Dandelion.Status.HOLD);
        assertThat(findDandelion.getCommunity()).isEqualTo(savedMember3.getAppConfig().getCommunity());
        assertThat(findDandelion.getMember().getSeq()).isNotEqualTo(savedMember3.getSeq());

        /**
         * 민들레 조회 이력 데이터 검증
         */
        assertThat(findMemberDandelionHistory.getMember().getSeq()).isEqualTo(savedMember3.getSeq());
        assertThat(findMemberDandelionHistory.getDandelion().getSeq()).isEqualTo(savedDandelion1.getSeq());
    }
}