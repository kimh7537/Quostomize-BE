package com.quostomize.quostomize_be.api.payment.dto;
import java.time.LocalDateTime;

public record PaymentRecordResponse(
        Long paymentRecordId,
        int industryType,
        String businessRegistrationNumber,
        Long totalPaymentAmount,
        Long cardSequenceId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
