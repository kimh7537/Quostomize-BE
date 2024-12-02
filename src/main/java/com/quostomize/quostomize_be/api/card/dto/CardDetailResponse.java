package com.quostomize.quostomize_be.api.card.dto;

import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        CardStatus cardStatus,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static CardDetailResponse fromEntity(CardDetail cardDetail) {
        return new CardDetailResponse(
                cardDetail.getCardSequenceId(),
                cardDetail.getCardNumber(),
                cardDetail.getCardBrand(),
                cardDetail.getIsAppCard(),
                cardDetail.getIsForeignBlocked(),
                cardDetail.getIsPostpaidTransport(),
                cardDetail.getExpirationDate(),
                cardDetail.getOptionalTerms(),
                cardDetail.getPaymentReceiptMethods(),
                cardDetail.getStatus(),
                cardDetail.getCreatedAt(),
                cardDetail.getModifiedAt()
        );
    }
}
