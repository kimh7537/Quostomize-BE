package com.quostomize.quostomize_be.api.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SendPhoneRequest(
        @NotNull(message = "전화번호를 입력해주세요.")
        @Size(min = 11, max = 11, message = "'-'없이 11자리의 전화번호를 입력해주세요.")
        String phoneNumber,

        @NotNull(message = "인증번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자여야 합니다.")
        String certificationNumber
) {
}
