package com.i3e3.mindlet.domain.dandelion.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Schema(name = "민들레 상태 변경 DTO", description = "민들레 상태 변경 API를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"status"})
public class DandelionStatusChangeDto {

}
