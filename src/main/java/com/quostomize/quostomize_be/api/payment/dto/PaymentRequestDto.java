package com.quostomize.quostomize_be.api.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record PaymentRequestDto (
    // 카드 번호
    @NotNull
    @NotBlank
    String card_number,

    // 품목들
    @NotNull
    @NotBlank
    Product[] products,

    // 사업자 번호
    @NotNull
    @NotBlank
    String businessRegistrationNumber,

    // 결제 일시
    @DateTimeFormat(pattern = "yyyy-mm-dd'T'HH:mm:ss")
    LocalDate date
) {

}
