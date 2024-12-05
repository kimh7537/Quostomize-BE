package com.quostomize.quostomize_be.api.payment.dto;
import com.quostomize.quostomize_be.domain.customizer.payment.entity.PaymentRecord;

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
    public static PaymentRecordResponse fromEntity(PaymentRecord record) {
        return new PaymentRecordResponse(
                record.getPaymentRecordId(),
                record.getIndustryType(),
                record.getBusinessRegistrationNumber(),
                record.getTotalPaymentAmount(),
                record.getCardDetail().getCardSequenceId(),
                record.getCreatedAt(),
                record.getModifiedAt()
        );
    }
}