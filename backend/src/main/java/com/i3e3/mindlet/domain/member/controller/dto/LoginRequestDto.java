package com.i3e3.mindlet.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(name = "로그인 요청 DTO", description = "로그인 API를 호출할 때 사용됩니다.")
@NoArgsConstructor
@Getter
@ToString(of = {"id", "password"})
public class LoginRequestDto {
}
