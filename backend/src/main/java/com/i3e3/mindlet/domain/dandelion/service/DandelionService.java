package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.service.dto.SeedCountDto;

public interface DandelionService {

    boolean isBlossomed(Long dandelionSeq);

    boolean isOwner(Long dandelionSeq, Long memberSeq);

    String changeDescription(Long dandelionSeq, String description);

    SeedCountDto getLeftSeedCount(Long memberSeq);

    boolean isReturn(Long dandelionSeq);
}
