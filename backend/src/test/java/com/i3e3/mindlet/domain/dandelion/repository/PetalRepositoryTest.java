package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
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
public class PetalRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private PetalRepository petalRepository;

    private Member member1;

    private Dandelion dandelion1;

    private Petal petal1;

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
    @DisplayName("꽃잎 식별키로 꽃잎 데이터 조회 - 데이터가 존재 할 경우")
    void findPetalBySeqSuccess() {
        // given
        memberRepository.save(member1);
        dandelionRepository.save(dandelion1);
        Petal savedPetal = petalRepository.save(petal1);
        em.flush();
        em.clear();

        // when
        Petal findPetal = petalRepository.findBySeq(savedPetal.getSeq())
                .orElse(null);

        // then
        assertThat(findPetal).isNotNull();
        assertThat(findPetal.getMessage()).isEqualTo(petal1.getMessage());
        assertThat(findPetal.getImagePath()).isEqualTo(petal1.getImagePath());
        assertThat(findPetal.getNation()).isEqualTo(petal1.getNation());
        assertThat(findPetal.getCity()).isEqualTo(petal1.getCity());
        assertThat(findPetal.getNationalFlagImagePath()).isEqualTo(petal1.getNationalFlagImagePath());
        assertThat(findPetal.getMember().getSeq()).isEqualTo(petal1.getMember().getSeq());
        assertThat(findPetal.getDandelion().getSeq()).isEqualTo(petal1.getDandelion().getSeq());
    }
}
