package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface DandelionRepositoryCustom {

    int countUsingSeed(Long memberSeq);

    List<Dandelion> findActiveDandelionListByMemberSeq(Long memberSeq);

    Optional<Dandelion> findRandomFlyingDandelionExceptMember(Member member);

    void updateHoldingDandelionToFlying(long minute);

    long countParticipationDandelions(Long memberSeq);
}
