package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberDandelionHistoryRepository;
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
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private MemberDandelionHistoryRepository memberDandelionHistoryRepository;

    private Member member1, member2, member3;

    private AppConfig appConfig1, appConfig2, appConfig3;

    private Dandelion dandelion1, dandelion2, dandelion3;

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
                .blossomedDate(LocalDate.parse("2022-05-01"))
                .community(member2.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member2)
                .build();

        dandelion3 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-05-02"))
                .community(member3.getAppConfig().getCommunity())
                .flowerSignNumber(1)
                .member(member3)
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

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - hold 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenHoldAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.HOLD);
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
    @DisplayName("사용중인 민들레 개수 조회 - return 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenReturn() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.RETURN);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - return 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenReturnAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.RETURN);
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
    @DisplayName("사용중인 민들레 개수 조회 - pending 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenPending() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.PENDING);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - pending 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenPendingAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.PENDING);
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
    @DisplayName("사용중인 민들레 개수 조회 - blossomed 상태의 민들레가 1개인 경우")
    void countUsingSeedWhenBlossomed() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(1);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - blossomed 상태의 민들레가 1개이지만 삭제된 경우")
    void countUsingSeedWhenBlossomedAndDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.BLOSSOMED);
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
    @DisplayName("사용중인 민들레 개수 조회 - blocked 상태의 민들레만 1개일 경우")
    void countUsingSeedWhenBlocked() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.BLOCKED);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("사용중인 민들레 개수 조회 - album 상태의 민들레만 1개일 경우")
    void countUsingSeedWhenAlbum() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion1.changeStatus(Dandelion.Status.ALBUM);
        dandelionRepository.save(dandelion1);
        em.flush();
        em.clear();

        // when
        int countUsingSeed = dandelionRepository.countUsingSeed(savedMember.getSeq());
        // then
        assertThat(countUsingSeed).isEqualTo(0);
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - 조회 성공")
    void getDandelionListInTheGarden() {
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
        dandelion2.changeStatus(Dandelion.Status.HOLD);
        dandelion3.changeStatus(Dandelion.Status.BLOSSOMED);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        Dandelion savedDandelion3 = dandelionRepository.save(dandelion3);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(3);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(1).getStatus()).isEqualTo(Dandelion.Status.HOLD);
        assertThat(dandelions.get(2).getStatus()).isEqualTo(Dandelion.Status.BLOSSOMED);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion1.getSeq());
        assertThat(dandelions.get(1).getSeq()).isEqualTo(savedDandelion2.getSeq());
        assertThat(dandelions.get(2).getSeq()).isEqualTo(savedDandelion3.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - 삭제된 민들레 조회 시도")
    void getDandelionListInTheGardenIsDeleted() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion1.delete();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findDandelionListByMemberSeq(savedMember.getSeq());

        // then
        assertThat(dandelions.size()).isEqualTo(1);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - album 상태의 민들레 조회 시도")
    void getDandelionListInTheGardenAlbum() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion1.changeStatus(Dandelion.Status.ALBUM);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(1);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - block 상태의 민들레 조회 시도")
    void getDandelionListInTheGardenBlock() {
        // given
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        dandelion1.changeStatus(Dandelion.Status.BLOCKED);
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(1);
        assertThat(dandelions.get(0).getStatus()).isEqualTo(Dandelion.Status.FLYING);
        assertThat(dandelions.get(0).getSeq()).isEqualTo(savedDandelion2.getSeq());
    }

    @Test
    @DisplayName("꽃밭 민들레 리스트 조회 - 삭제된 유저의 민들레 조회 시도")
    void getDandelionListInTheGardenMemberIsDeleted() {
        // given
        member1.delete();
        Member savedMember = memberRepository.save(member1);
        dandelion2 = Dandelion.builder()
                .blossomedDate(LocalDate.parse("2022-04-30"))
                .community(Community.WORLD)
                .flowerSignNumber(2)
                .member(member1)
                .build();
        Dandelion savedDandelion1 = dandelionRepository.save(dandelion1);
        Dandelion savedDandelion2 = dandelionRepository.save(dandelion2);
        em.flush();
        em.clear();

        // when
        List<Dandelion> dandelions = dandelionRepository.findDandelionListByMemberSeq(savedMember.getSeq());
        // then
        assertThat(dandelions.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 성공")
    void findRandomDandelionExceptMemberSuccess() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build());
        }

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion.getSeq()).isEqualTo(members.get(1).getDandelions().get(0).getSeq());
    }

    @Test
    @DisplayName("특정 회원의 민들레를 제외하고 민들레 데이터 랜덤 조회 - 성공 : 이미 잡은 민들레가 있을 경우")
    void findRandomDandelionExceptMemberWhenCatchOneBefore() {
        // given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Member member = Member.builder()
                    .id("아이디" + i)
                    .password("패스워드" + i)
                    .build();
            members.add(member);
            memberRepository.save(member);

            appConfigRepository.save(AppConfig.builder()
                    .member(member)
                    .language(AppConfig.Language.ENGLISH)
                    .build());

            dandelionRepository.save(Dandelion.builder()
                    .blossomedDate(LocalDate.parse("2022-04-2" + i))
                    .community(Community.KOREA)
                    .flowerSignNumber(1)
                    .member(member)
                    .build());
        }

        em.persist(MemberDandelionHistory.builder()
                .member(members.get(0))
                .dandelion(members.get(1).getDandelions().get(0))
                .build());

        em.flush();
        em.clear();

        // when
        Dandelion findRandomDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(members.get(0))
                .orElse(null);

        // then
        assertThat(findRandomDandelion.getSeq()).isEqualTo(members.get(2).getDandelions().get(0).getSeq());
    }
}