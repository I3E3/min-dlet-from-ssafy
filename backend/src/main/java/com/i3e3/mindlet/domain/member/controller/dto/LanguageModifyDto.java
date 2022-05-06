package com.i3e3.mindlet.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Schema(name = "언어 변경 DTO", description = "언어 변경 API 를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"language"})
public class LanguageModifyDto {

}