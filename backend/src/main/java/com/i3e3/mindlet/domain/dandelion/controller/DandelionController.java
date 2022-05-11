package com.i3e3.mindlet.domain.dandelion.controller;

import com.i3e3.mindlet.domain.dandelion.controller.dto.DandelionDescriptionModifyDto;
import com.i3e3.mindlet.domain.dandelion.controller.dto.DandelionRegisterDto;
import com.i3e3.mindlet.domain.dandelion.controller.dto.DandelionStatusChangeDto;
import com.i3e3.mindlet.domain.dandelion.controller.dto.DandelionTagRegisterDto;
import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.service.DandelionService;
import com.i3e3.mindlet.domain.dandelion.service.TagService;
import com.i3e3.mindlet.domain.dandelion.service.dto.DandelionSeedDto;
import com.i3e3.mindlet.domain.dandelion.service.dto.SeedCountDto;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dandelions")
public class DandelionController {

    private final DandelionService dandelionService;

    private final TagService tagService;

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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PatchMapping("/{dandelionSeq}/description")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeDescription(@PathVariable Long dandelionSeq,
                                                   @Validated @RequestBody DandelionDescriptionModifyDto modifyDto) {
        Long findMemberSeq = AuthenticationUtil.getMemberSeq();

        if (dandelionService.isBlossomed(dandelionSeq) &&
                dandelionService.isOwner(dandelionSeq, findMemberSeq)) {
            dandelionService.changeDescription(dandelionSeq, modifyDto.getDescription());
        } else {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        return BaseResponseDto.<Void>builder()
                .build();
    }

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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @GetMapping("/leftover-seed-count")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<SeedCountDto> getSeedCount() {
        Long findMemberSeq = AuthenticationUtil.getMemberSeq();

        SeedCountDto leftSeedCount = dandelionService.getLeftSeedCount(findMemberSeq);

        return BaseResponseDto.<SeedCountDto>builder()
                .data(leftSeedCount)
                .build();
    }

    @Operation(
            summary = "상태 변경 API",
            description = "인증 토큰, 민들레 식별키, 상태를 전달받고 민들레 상태을 변경합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상태 변경 성공",
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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PatchMapping("/{dandelionSeq}/status")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponseDto<Void> changeStatus(@PathVariable Long dandelionSeq,
                                              @RequestBody DandelionStatusChangeDto modifyDto) {
        Long findMemberSeq = AuthenticationUtil.getMemberSeq();
        Dandelion.Status status = null;

        try {
            status = Dandelion.Status.valueOf(modifyDto.getStatus());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        if (dandelionService.isOwner(dandelionSeq, findMemberSeq)) {
            if (status.equals(Dandelion.Status.ALBUM) && dandelionService.isBlossomed(dandelionSeq)
                    || status.equals(Dandelion.Status.BLOSSOMED) && dandelionService.isReturn(dandelionSeq)) {
                dandelionService.changeStatus(dandelionSeq, status);
            } else {
                throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
            }
        } else {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }
        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "민들레 태그 삭제 API 기능 추가",
            description = "인증 토큰, 민들레 식별키, 태그 식별키를 전달받고 민들레 태그를 삭제합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "태그 삭제 성공",
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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @DeleteMapping("/{dandelionSeq}/tags/{tagSeq}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponseDto<Void> deleteDandelionTag(@PathVariable Long dandelionSeq, @PathVariable Long tagSeq) {
        Long memberSeq = AuthenticationUtil.getMemberSeq();

        dandelionService.deleteTag(tagSeq, memberSeq);

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "민들레 태그 추가 API 기능 추가",
            description = "인증 토큰, 민들레 식별키, 태그 값을 전달받고 민들레 태그를 추가합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "태그 추가 성공",
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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping("/{dandelionSeq}/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponseDto<Void> registerDandelionTag(@PathVariable Long dandelionSeq, @Validated @RequestBody DandelionTagRegisterDto tagRegisterDto) {
        Long memberSeq = AuthenticationUtil.getMemberSeq();

        if (dandelionService.isParticipated(dandelionSeq, memberSeq) &&
                (dandelionService.isBlossomed(dandelionSeq) || dandelionService.isAlbum(dandelionSeq))) {
            tagService.registerDandelionTag(dandelionSeq, memberSeq, tagRegisterDto.getName());
        } else {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "민들레 삭제 API 기능 추가",
            description = "인증 토큰, 민들레 식별키를 전달받고 민들레를 삭제합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "민들레 삭제 성공",
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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @DeleteMapping("/{dandelionSeq}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponseDto<Void> deleteDandelion(@PathVariable Long dandelionSeq) {
        Long memberSeq = AuthenticationUtil.getMemberSeq();

        if (!dandelionService.isOwner(dandelionSeq, memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        dandelionService.deleteDandelion(dandelionSeq, memberSeq);

        return BaseResponseDto.<Void>builder()
                .build();
    }

    @Operation(
            summary = "민들레씨 잡기 API 기능 추가",
            description = "인증 토큰을 전달받고 민들레씨 정보를 반환합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "민들레씨 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponseDto.class))),
            @ApiResponse(
                    responseCode = "204",
                    description = "민들레씨 없음",
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
    @GetMapping("/random")
    public ResponseEntity<BaseResponseDto<DandelionSeedDto>> catchDandelionSeed() {
        Long findMemberSeq = AuthenticationUtil.getMemberSeq();
        DandelionSeedDto dandelionSeedDto = dandelionService.getDandelionSeedDto(findMemberSeq);

        HttpStatus status = dandelionSeedDto == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return new ResponseEntity<>(BaseResponseDto.<DandelionSeedDto>builder()
                .data(dandelionSeedDto)
                .build(), status);
    }

    @Operation(
            summary = "민들레 HOLD 해제 API 기능 추가",
            description = "인증 토큰, 민들레 식별키를 전달받고 HOLD 상태를 FLYING 으로 변경합니다.",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상태 변경 성공",
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
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{dandelionSeq}/status-flying")
    public BaseResponseDto<Void> holdUnlock(@PathVariable("dandelionSeq") Long dandelionSeq) {
        Long findMemberSeq = AuthenticationUtil.getMemberSeq();

        if (!dandelionService.isHold(dandelionSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else if (!dandelionService.isMostRecentParticipant(dandelionSeq, findMemberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else {
            dandelionService.changeStatus(dandelionSeq, Dandelion.Status.FLYING);
        }

        return BaseResponseDto.<Void>builder()
                .build();
    }


    @Operation(
            summary = "민들레씨 생성 후 날리기 API 기능 추가",
            description = "사용자로 부터 데이터를 받아 민들레씨를 생성후 날린다",
            tags = {"dandelion"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "민들레 생성 성공",
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
    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponseDto<Void> registerDandelion(@Validated @RequestPart(value = "dandelionRegisterForm") DandelionRegisterDto dandelionRegisterDto,
                                                   @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Long memberSeq = AuthenticationUtil.getMemberSeq();

        if (!LocalDate.parse(dandelionRegisterDto.getBlossomedDate()).isAfter(LocalDate.now().plusDays(1))) {
            throw new IllegalStateException(ErrorMessage.INVALID_DATE_REQUEST.getMessage());
        } else if ((dandelionRegisterDto.getMessage() == null && imageFile == null) || !registerPossible(memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else {
            dandelionRegisterDto.addFile(imageFile);
            dandelionService.createDandelion(memberSeq, dandelionRegisterDto.toSvcDto());
        }

        return BaseResponseDto.<Void>builder()
                .build();
    }

    private boolean registerPossible(Long memberSeq) {
        return dandelionService.getLeftSeedCount(memberSeq).getLeftSeedCount() > 0;
    }
}
