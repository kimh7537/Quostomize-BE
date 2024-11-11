package com.quostomize.quostomize_be.api.cardBenefit.dto;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import java.time.LocalDateTime;

public record CardBenefitResponse(
        Long benefitId,
        Integer benefitRate,
        Boolean isActive,
        Long cardId,
        Long upperCategoryId,
        Long lowerCategoryId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static CardBenefitResponse from(CardBenefit cardBenefit) {
        return new CardBenefitResponse(
                cardBenefit.getBenefitId(),
                cardBenefit.getBenefitRate(),
                cardBenefit.getIsActive(),
                cardBenefit.getCard().getCardId(),
                cardBenefit.getUpperCategory().getBenefitCommonId(),
                cardBenefit.getLowerCategory().getBenefitCommonId(),
                cardBenefit.getCreatedAt(),
                cardBenefit.getModifiedAt()
        );
    }
}