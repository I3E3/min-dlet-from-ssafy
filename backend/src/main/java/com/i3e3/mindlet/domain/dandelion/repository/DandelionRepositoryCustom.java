package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;

import java.util.List;

public interface DandelionRepositoryCustom {

    int countUsingSeed(Long memberSeq);

    List<Dandelion> findDandelionListByMemberSeq(Long memberSeq);
}
