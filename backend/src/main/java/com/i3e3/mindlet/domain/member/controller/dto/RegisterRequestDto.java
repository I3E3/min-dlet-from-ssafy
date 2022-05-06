package com.i3e3.mindlet.domain.member.controller.dto;

import com.i3e3.mindlet.domain.member.service.dto.MemberRegisterDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@ToString(of = {"id", "password"})
public class RegisterRequestDto {

    @Schema(title = "아이디", description = "아이디입니다.", example = "good", minLength = 4, maxLength = 12, required = true)
    @Pattern(message = "", regexp = "^[0-9|a-z|\\s]{4,12}$")
    private String id;

    @Schema(title = "패스워드", description = "패스워드입니다.", example = "test12#$", minLength = 8, maxLength = 20, required = true)
    @Pattern(message = "", regexp = "^((?=.*[a-z])(?=.*\\d)((?=.*\\W))|(?=.*\\W)((?=.*\\d)|(?=.*[a-z]))).{8,20}$")
    private String password;

    @Builder
    public RegisterRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public MemberRegisterDto toServiceDto() {
        return MemberRegisterDto.builder()
                .id(id)
                .password(password)
                .build();
    }
}
