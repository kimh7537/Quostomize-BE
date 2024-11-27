package com.quostomize.quostomize_be.api.card.dto;

import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;

import java.time.LocalDate;

public record CardDetailResponse(
        Long cardSequenceId,
        String cardNumber,
        int cardBrand,
        Boolean isAppCard,
        Boolean isForeignBlocked,
        Boolean isPostpaidTransport,
        LocalDate expirationDate,
        int optionalTerms,
        int paymentReceiptMethods,
        CardStatus cardStatus
) {
}
