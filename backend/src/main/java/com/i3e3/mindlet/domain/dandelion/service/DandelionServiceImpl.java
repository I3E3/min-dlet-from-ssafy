package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.service.dto.SeedCountDto;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.dandelion.DandelionConst;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DandelionServiceImpl implements DandelionService {

    private final DandelionRepository dandelionRepository;
    private final MemberRepository memberRepository;

    @Override
    public boolean isBlossomed(Long dandelionSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.BLOSSOMED;
    }

    @Override
    public boolean isOwner(Long dandelionSeq, Long memberSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getMember().getSeq() == memberSeq;
    }

    @Transactional
    @Override
    public String changeDescription(Long dandelionSeq, String description) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        findDandelion.changeDescription(description);
        return findDandelion.getDescription();
    }

    @Override
    public SeedCountDto getLeftSeedCount(Long memberSeq) {

        if (!memberRepository.existsBySeq(memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        Integer countUsingSeed = dandelionRepository.countUsingSeed(memberSeq);
        int maxUsingDandelionCount = DandelionConst.MAX_USING_DANDELION_COUNT.getValue();
        if (countUsingSeed > maxUsingDandelionCount) {
            throw new IllegalStateException(ErrorMessage.MORE_THAN_MAX_COUNT.getMessage());
        }

        int leftSeedCount = maxUsingDandelionCount - countUsingSeed;

        return SeedCountDto.builder()
                .leftSeedCount(leftSeedCount)
                .build();
    }

    @Override
    public boolean isReturn(Long dandelionSeq) {

        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.RETURN;
    }

    @Transactional
    @Override
    public void changeStatus(Long dandelionSeq, Dandelion.Status status) {

        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        findDandelion.changeStatus(status);
    }
}
