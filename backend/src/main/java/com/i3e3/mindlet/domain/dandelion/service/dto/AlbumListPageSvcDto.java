package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(of = {"totalDandelionCount", "nowPageNum", "totalPageNum", "dandelionInfos"})
public class AlbumListPageSvcDto {

    private Long totalDandelionCount;

    private int nowPageNum;

    private int totalPageNum;

    List<AlbumListPageSvcDto.dandelionInfo> dandelionInfos;

    @Builder
    public AlbumListPageSvcDto(Long totalDandelionCount,
                                int nowPageNum,
                                int totalPageNum,
                                List<AlbumListPageSvcDto.dandelionInfo> dandelionInfos) {

        this.totalDandelionCount = totalDandelionCount;
        this.nowPageNum = nowPageNum;
        this.totalPageNum = totalPageNum;
        this.dandelionInfos = dandelionInfos;
    }

    @Getter
    @ToString(of = {"dandelionSeq", "description"})
    public static class dandelionInfo {

        private Long DandelionSeq;

        private String description;

        @Builder
        public dandelionInfo(Long dandelionSeq, String description) {
            this.DandelionSeq = dandelionSeq;
            this.description = description;
        }
    }
}
