package com.i3e3.mindlet.domain.dandelion.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Schema(name = "민들레 꽃말 수정 DTO", description = "민들레 꽃말 수정 API를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"description"})
public class DandelionDescriptionModifyDto {

    @Length(min = 1, max = 20)
    @NotBlank
    private String description;
}
