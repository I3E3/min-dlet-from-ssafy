package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.repository.PetalRepository;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.dandelion.service.dto.*;
import com.i3e3.mindlet.domain.file.service.FileService;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import com.i3e3.mindlet.domain.member.repository.MemberDandelionHistoryRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.dandelion.DandelionConst;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DandelionServiceImpl implements DandelionService {

    private final DandelionRepository dandelionRepository;
    private final MemberRepository memberRepository;

    private final TagRepository tagRepository;

    private final PetalRepository petalRepository;

    private final MemberDandelionHistoryRepository memberDandelionHistoryRepository;

    private final FileService fileService;

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

        return findDandelion.getMember().getSeq().equals(memberSeq);
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

    @Transactional
    @Override
    public void deleteTag(Long tagSeq, Long memberSeq) {
        Tag findTag = tagRepository.findBySeq(tagSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        if (!findTag.getMember().getSeq().equals(memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else {
            tagRepository.delete(findTag);
        }
    }

    @Transactional
    @Override
    public void deleteDandelion(Long dandelionSeq, Long memberSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        if (!findDandelion.getMember().getSeq().equals(memberSeq) ||
                findDandelion.getMember().isDeleted()) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else {
            findDandelion.delete();
            findDandelion.getPetals().forEach((petal -> petal.delete()));
            findDandelion.getTags().forEach(tag -> tagRepository.delete(tag));
        }
    }

    @Override
    public boolean isParticipated(Long dandelionSeq, Long memberSeq) {
        return petalRepository.existsPetalByDandelionSeqAndMemberSeq(dandelionSeq, memberSeq);
    }

    @Override
    public boolean isAlbum(Long dandelionSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.ALBUM;
    }

    @Transactional
    @Override
    public List<ResponseGardenInfoDto> getGardenInfoList(Long memberSeq) {

        if (!memberRepository.existsBySeq(memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        List<Dandelion> dandelions = dandelionRepository.findActiveDandelionListByMemberSeq(memberSeq);
        List<ResponseGardenInfoDto> responseGardenInfos = new ArrayList<ResponseGardenInfoDto>();

        for (Dandelion dandelion : dandelions) {
            responseGardenInfos.add(
                    ResponseGardenInfoDto.builder()
                            .blossomedDate(dandelion.getBlossomedDate())
                            .description(dandelion.getDescription())
                            .flowerSignNumber(dandelion.getFlowerSignNumber())
                            .seq(dandelion.getSeq())
                            .status(String.valueOf(dandelion.getStatus()))
                            .build()
            );
        }

        return responseGardenInfos;
    }

    @Transactional
    @Override
    public DandelionSeedDto getDandelionSeedDto(Long memberSeq) {
        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));
        Dandelion findDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(findMember)
                .orElse(null);

        if (findDandelion == null) {
            return null;
        }

        List<Petal> petals = findDandelion.getPetals();
        List<DandelionSeedDto.PetalInfo> petalInfos = new ArrayList<>();
        for (Petal petal : petals) {
            if (petal.isDeleted()) continue;

            petalInfos.add(DandelionSeedDto.PetalInfo.builder()
                    .seq(petal.getSeq())
                    .message(petal.getMessage())
                    .imageUrlPath(petal.getImagePath())
                    .nation(petal.getNation())
                    .createdDate(petal.getCreatedDate())
                    .build());
        }
        petalInfos.sort(Comparator.comparing(DandelionSeedDto.PetalInfo::getCreatedDate));

        MemberDandelionHistory.builder()
                .member(findMember)
                .dandelion(findDandelion)
                .build();

        findDandelion.changeStatus(Dandelion.Status.HOLD);

        return DandelionSeedDto.builder()
                .seq(findDandelion.getSeq())
                .petalInfos(petalInfos)
                .build();
    }

    @Transactional
    @Override
    public void createDandelion(Long memberSeq, DandelionCreateSvcDto dandelionCreateSvcDto) throws IOException {
        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        int flowerSignNumber = getFlowerSignNumber(findMember.getSeq());

        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(dandelionCreateSvcDto.getBlossomedDate())
                .community(findMember.getAppConfig().getCommunity())
                .flowerSignNumber(flowerSignNumber)
                .member(findMember)
                .build();

        createPetal(findMember, newDandelion, dandelionCreateSvcDto.toPetalCreateSvcDto());
    }

    private Petal createPetal(Member member, Dandelion dandelion, PetalCreateSvcDto petalCreateSvcDto) throws IOException {

        if (dandelion == null || dandelion.isDeleted()) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        if (member == null || member.isDeleted()) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        if (petalRepository.existsPetalByDandelionSeqAndMemberSeq(dandelion.getSeq(),member.getSeq())) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        String filePath = null;

        if (petalCreateSvcDto.getImageFile() != null) {
            filePath = fileService.s3Upload(petalCreateSvcDto.getImageFile());
        }

        return Petal.builder()
                .message(petalCreateSvcDto.getMessage())
                .imagePath(filePath)
                .nation("KOREA")
                .dandelion(dandelion)
                .member(member)
                .build();
    }

    private int getFlowerSignNumber(Long memberSeq) {
        List<Dandelion> findActiveDandelions = dandelionRepository.findActiveDandelionListByMemberSeq(memberSeq);

        if (findActiveDandelions != null && findActiveDandelions.size() == 5) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        int flowerSign = 1;

        if (findActiveDandelions != null && findActiveDandelions.size() != 0) {
            TreeSet<Integer> ts = new TreeSet<>(List.of(1, 2, 3, 4, 5));

            findActiveDandelions.forEach(dandelion -> ts.remove(dandelion.getFlowerSignNumber()));

            flowerSign = ts.pollFirst();
        }
        return flowerSign;
    }
}
