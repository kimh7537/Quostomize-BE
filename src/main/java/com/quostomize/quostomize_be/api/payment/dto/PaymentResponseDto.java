package com.quostomize.quostomize_be.api.payment.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record PaymentResponseDto (
        Long payment_id,

        // 카드 번호
        String card_number,

        // 사업자 번호
        String businessRegistrationNumber,

        // 총 결제 금액
        String totalPrice,

        // 결제 일시
        @DateTimeFormat(pattern = "yyyy-mm-dd'T'HH:mm:ss")
        LocalDateTime date
) {

}
