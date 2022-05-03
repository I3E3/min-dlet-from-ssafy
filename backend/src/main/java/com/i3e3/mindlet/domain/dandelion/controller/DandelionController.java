package com.i3e3.mindlet.domain.dandelion.controller;

import com.i3e3.mindlet.domain.dandelion.controller.dto.DandelionDescriptionModifyDto;
import com.i3e3.mindlet.domain.dandelion.service.DandelionService;
import com.i3e3.mindlet.domain.dandelion.service.dto.SeedCountDto;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.dto.BaseResponseDto;
import com.i3e3.mindlet.global.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dandelions")
public class DandelionController {

    private final DandelionService dandelionService;

    /**
     * @TODO OAuth
     */
    @Operation(
            summary = "꽃말 수정 API",
            description = "인증 토큰, 민들레 식별키, 꽃말을 전달받고 민들레 꽃말을 수정합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "꽃말 수정 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "꽃말 데이터 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("/{dandelionSeq}/description")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeDescription(@PathVariable Long dandelionSeq,
                                                   @Validated @RequestBody DandelionDescriptionModifyDto modifyDto) {
        /**
         * 회원 식별키는 OAuth에서 뽑아야 한다.
         */
        Long memberSeq = null;

        if (dandelionService.isBlossomed(dandelionSeq) &&
                dandelionService.isOwner(dandelionSeq, memberSeq)) {
            dandelionService.changeDescription(dandelionSeq, modifyDto.getDescription());
        } else {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        return BaseResponseDto.<Void>builder()
                .build();
    }
    
    /**
     * @TODO OAuth
     */
    @Operation(
            summary = "남은 씨앗 개수 조회 API",
            description = "인증 토큰을 전달받고 남은 씨앗 개수를 반환합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "남은 씨앗 개수 반환 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/leftover-seed-count")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<SeedCountDto> getSeedCount() {

        /**
         * 회원 식별키는 OAuth에서 뽑아야 한다.
         */
        Long memberSeq = null;

        SeedCountDto leftSeedCount = dandelionService.getLeftSeedCount(memberSeq);

        return BaseResponseDto.<SeedCountDto>builder()
                .data(leftSeedCount)
                .build();
    }
}
