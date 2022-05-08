package com.i3e3.mindlet.domain.dandelion.controller;

import com.i3e3.mindlet.domain.dandelion.service.DandelionService;
import com.i3e3.mindlet.domain.dandelion.service.dto.ResponseGardenInfoDto;
import com.i3e3.mindlet.global.dto.BaseResponseDto;
import com.i3e3.mindlet.global.dto.ErrorResponseDto;
import com.i3e3.mindlet.global.util.AuthenticationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/garden")
public class GardenController {
    DandelionService dandelionService;

    @Operation(
            summary = "꽃밭 조회 API 기능 추가",
            description = "인증 토큰을 전달받고 꽃밭에 심어진 민들레 정보 리스트를 반환합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<List<ResponseGardenInfoDto>> gardenInfo() {

        Long memberSeq = AuthenticationUtil.getMemberSeq();

        List<ResponseGardenInfoDto> gardenInfoDtoListList = dandelionService.getGardenInfoList(memberSeq);

        return BaseResponseDto.<List<ResponseGardenInfoDto>>builder()
                .data(gardenInfoDtoListList)
                .build();
    }
}
