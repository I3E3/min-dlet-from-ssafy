package com.i3e3.mindlet.domain.dandelion.controller.dto;

import com.i3e3.mindlet.domain.dandelion.service.dto.DandelionCreateSvcDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Schema(name = "민들레씨 생성 요청 DTO", description = "민들레씨 생성 후 날리기 API 를 호출할 때 사용됩니다.")
@NoArgsConstructor
@Getter
@ToString(of = {"message", "blossomedDate", "imageFile"})
public class DandelionRegisterDto {

    @Schema(title = "메시지", description = "민들레에 담길 메시지입니다.", example = "반가워요")
    @Length(max = 100)
    private String message;

    @Schema(title = "민들레씨 반환 날짜", description = "사용자가 지정한 민들레 반환 날짜 입니다.", example = "2022-05-16")
    @Pattern(message = "날짜 표현이 잘못 되었습니다.", regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$")
    private String blossomedDate;

    @Schema(title = "사진", description = "사용자가 올린 사진 파일입니다.")
    private MultipartFile imageFile;

    public DandelionCreateSvcDto toSvcDto() {
        return DandelionCreateSvcDto.builder()
                .message(message)
                .blossomedDate(LocalDate.parse(blossomedDate))
                .imageFile(imageFile)
                .nation("KOREA")
                .build();
    }
}
