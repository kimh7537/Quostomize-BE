package com.quostomize.quostomize_be.api.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SmsRequest(
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다.")
        String phone,

        @NotNull(message = "인증번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자여야 합니다.")
        String certificationNumber
) {
}