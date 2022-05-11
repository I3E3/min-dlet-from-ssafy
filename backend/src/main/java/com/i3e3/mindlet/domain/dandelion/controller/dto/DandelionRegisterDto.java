package com.i3e3.mindlet.domain.dandelion.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(name = "민들레씨 생성 요청 DTO", description = "민들레씨 생성 후 날리기 API 를 호출할 때 사용됩니다.")
@NoArgsConstructor
@Getter
@ToString(of = {"message", "blossomedDate", "imageFile"})
public class DandelionRegisterDto {
}
