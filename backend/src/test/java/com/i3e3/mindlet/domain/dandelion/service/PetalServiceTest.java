package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.admin.entity.Report;
import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.repository.PetalRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PetalServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private PetalRepository petalRepository;

    @Autowired
    private PetalService petalService;

    private Member member1, member2;

    private Dandelion dandelion1;

    private Petal petal1;


    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dandelionRepository.deleteAll();
        petalRepository.deleteAll();

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

        petal1 = Petal.builder()
                .message("hello")
                .imagePath("imagePath")
                .nation("KOREA")
                .city("SEOUL")
                .nationalFlagImagePath("nationalFlagImagePath")
                .dandelion(dandelion1)
                .member(member1)
                .build();
    }

    @Test
    @DisplayName("신고 접수 - 리스트 크기가 0일 경우")
    void addReportRequestedWhenNotingReports() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(1);
        assertThat(reports.get(0).getStatus()).isEqualTo(Report.Status.REQUESTED);
        assertThat(reports.get(0).getMember().getSeq()).isEqualTo(member2.getSeq());
        assertThat(reports.get(0).getPetal().getSeq()).isEqualTo(petal1.getSeq());
        assertThat(reports.get(0).getReason()).isEqualTo(Report.Reason.AD);
    }

    @Test
    @DisplayName("신고 접수 - 리스트가 있을 경우 상태가 rejected 일 경우")
    void PassWhenStatusRejected() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        Report newReport = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport.changeStatus(Report.Status.REJECTED, null);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(1);
        assertThat(reports.get(0).getStatus()).isEqualTo(Report.Status.REJECTED);
        assertThat(reports.get(0).getMember().getSeq()).isEqualTo(member2.getSeq());
        assertThat(reports.get(0).getPetal().getSeq()).isEqualTo(petal1.getSeq());
        assertThat(reports.get(0).getReason()).isEqualTo(Report.Reason.AD);
    }

    @Test
    @DisplayName("신고 접수 - rejected가 아니고 리스트 크기가 1 이하일 경우")
    void addReportRequestedWhenHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        Report newReport = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport.changeStatus(Report.Status.REQUESTED, null);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(2);
        assertThat(reports.get(1).getStatus()).isEqualTo(Report.Status.REQUESTED);
        assertThat(reports.get(1).getMember().getSeq()).isEqualTo(member2.getSeq());
        assertThat(reports.get(1).getPetal().getSeq()).isEqualTo(petal1.getSeq());
        assertThat(reports.get(1).getReason()).isEqualTo(Report.Reason.AD);
    }

    @Test
    @DisplayName("신고 접수 - rejected가 아니고 리스트 크기가 2 이상일 경우")
    void addReportPendingWhenHasData() {
        // given
        memberRepository.save(member1);
        memberRepository.save(member2);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        Report newReport1 = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport1.changeStatus(Report.Status.REQUESTED, null);
        Report newReport2 = Report.builder()
                .reason(Report.Reason.AD)
                .member(member2)
                .petal(petal1)
                .build();
        newReport2.changeStatus(Report.Status.REQUESTED, null);
        em.flush();
        em.clear();

        // when
        petalService.reportPetal(member2.getSeq(), savedPetal.getSeq(), Report.Reason.AD);
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);
        List<Report> reports = findPetal.getReports();

        // then
        assertThat(reports.size()).isEqualTo(3);
        assertThat(reports.get(0).getStatus()).isEqualTo(Report.Status.PENDING);
        assertThat(reports.get(1).getStatus()).isEqualTo(Report.Status.PENDING);
        assertThat(reports.get(2).getStatus()).isEqualTo(Report.Status.PENDING);
    }
}
