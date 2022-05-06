package com.i3e3.mindlet.domain.member.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Schema(name = "로그인 응답 DTO", description = "로그인에 성공하여 회원 정보를 반환하기 위해 사용됩니다.")
@Getter
@ToString
public class MemberInfoDto {
}
