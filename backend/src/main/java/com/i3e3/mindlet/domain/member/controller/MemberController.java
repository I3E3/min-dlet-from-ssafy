package com.i3e3.mindlet.domain.member.controller;

import com.i3e3.mindlet.domain.member.controller.dto.CommunityModifyDto;
import com.i3e3.mindlet.domain.member.controller.dto.SoundModifyDto;
import com.i3e3.mindlet.domain.member.service.MemberService;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.dto.BaseResponseDto;
import com.i3e3.mindlet.global.dto.ErrorResponseDto;
import com.i3e3.mindlet.global.enums.Community;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    /**
     * @TODO OAuth
     */
    @Operation(
            summary = "커뮤니티 변경 API",
            description = "인증 토큰, 회원 식별키, 커뮤니티 값을 받아 커뮤니티를 수정합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "커뮤니티 변경 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터 검증 실패",
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
    @PatchMapping("/{memberSeq}/community")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeCommunity(@PathVariable Long memberSeq, @Validated @RequestBody CommunityModifyDto modifyDto) {

        Long findMemberSeq = memberSeq;

        if (findMemberSeq == null || !findMemberSeq.equals(memberSeq)) {
            throw new AccessDeniedException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        Community community = null;
        try {
            community = Community.valueOf(modifyDto.getCommunity());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        memberService.changeCommunity(memberSeq, community);

        return BaseResponseDto.<Void>builder()
                .build();
    }

    /**
     * @TODO OAuth
     */
    @Operation(
            summary = "사운드 음소거 설정 API",
            description = "인증 토큰, 회원 식별키, 사운드 재생여부값을 받아 사운드를 On/Off 합니다.",
            tags = {"member"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사운드 변경 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터 검증 실패",
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
    @PatchMapping("/{memberSeq}/sound-off")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeSound(@PathVariable Long memberSeq, @Validated @RequestBody SoundModifyDto modifyDto) {
        Long findMemberSeq = memberSeq;

        if (findMemberSeq == null || !findMemberSeq.equals(memberSeq)) {
            throw new AccessDeniedException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        memberService.changeSound(memberSeq, modifyDto.isSoundOff());

        return BaseResponseDto.<Void>builder()
                .build();
    }
}
