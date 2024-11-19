package com.quostomize.quostomize_be.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JoinRequest(

        @NotNull(message = "아이디를 입력해주세요")
        String memberLoginId,

        @Email(message = "올바른 형식의 메일 주소를 입력해주세요.")
        @NotBlank(message = "공백이 아닌 올바른 형식의 메일 주소를 입력해주세요.")
        String memberEmail,

        @Size(min = 8, max = 16, message = "비밀번호는 8~16자리여야합니다.")
        @NotNull(message = "비밀번호를 입력해주세요.")
        String memberPassword,

        @NotBlank(message = "올바른 형식의 이름을 입력해주세요.")
        String memberName,

        @NotNull(message = "전화번호를 입력해주세요.")
        @Size(min = 11, max = 11, message = "'-'없이 11자리의 전화번호를 입력해주세요.")
        String memberPhoneNumber,
        
        @NotNull(message = "2차 비밀번호를 입력해주세요")
        @Size(min = 6, max = 6)
        String secondaryAuthCode,

        @NotNull(message = "주민번호를 입력해주세요")
        String residenceNumber,

        @NotNull(message = "우편번호를 입력해주세요")
        String zipCode,

        @NotNull(message = "주소를 입력해주세요")
        String memberAddress,

        @NotNull(message = "상세주소를 입력해주세요")
        String memberDetailAddress

        
) {
}
