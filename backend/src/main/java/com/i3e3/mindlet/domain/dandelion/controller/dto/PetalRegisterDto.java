package com.i3e3.mindlet.domain.dandelion.controller.dto;

import com.i3e3.mindlet.domain.dandelion.service.dto.PetalCreateSvcDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "꽃잎 추가 요청 DTO", description = "꽃잎 추가 API 를 호출할 때 사용됩니다.")
@Getter
@ToString(of = {"message", "imageFile"})
public class PetalRegisterDto {

    @Schema(title = "메시지", description = "민들레에 담길 메시지입니다.", example = "반가워요")
    @Length(max = 100)
    private String message;

    @Schema(title = "사진", description = "사용자가 올린 사진 파일입니다.")
    private MultipartFile imageFile;

    public PetalCreateSvcDto toSvcDto() {
        return PetalCreateSvcDto.builder()
                .message(message)
                .imageFile(imageFile)
                .nation("KOREA")
                .build();
    }
}
