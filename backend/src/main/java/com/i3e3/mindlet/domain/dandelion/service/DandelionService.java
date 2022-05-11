package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.service.dto.AlbumListPageSvcDto;
import com.i3e3.mindlet.domain.dandelion.service.dto.DandelionSeedDto;
import com.i3e3.mindlet.domain.dandelion.service.dto.ResponseGardenInfoDto;
import com.i3e3.mindlet.domain.dandelion.service.dto.SeedCountDto;

import java.util.List;

public interface DandelionService {

    boolean isBlossomed(Long dandelionSeq);

    boolean isOwner(Long dandelionSeq, Long memberSeq);

    String changeDescription(Long dandelionSeq, String description);

    SeedCountDto getLeftSeedCount(Long memberSeq);

    boolean isReturn(Long dandelionSeq);

    void changeStatus(Long dandelionSeq, Dandelion.Status status);

    void deleteTag(Long tagSeq, Long memberSeq);

    List<ResponseGardenInfoDto> getGardenInfoList(Long memberSeq);

    void deleteDandelion(Long dandelionSeq, Long memberSeq);

    boolean isParticipated(Long dandelionSeq, Long memberSeq);

    boolean isAlbum(Long dandelionSeq);

    DandelionSeedDto getDandelionSeedDto(Long memberSeq);

    AlbumListPageSvcDto getAlbumInfo(Long memberSeq, int page, int size);
}
