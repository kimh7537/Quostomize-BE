package com.quostomize.quostomize_be.api.cardBenefit.dto;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import java.time.LocalDateTime;

public record CardBenefitResponse(
        Long benefitId,
        Integer benefitRate,
        Boolean isActive,
        Long cardSequenceId,
        Long upperCategoryId,
        Long lowerCategoryId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static CardBenefitResponse from(CardBenefit cardBenefit) {
       Long cardBenefitLowerId =  (cardBenefit.getLowerCategory() == null) ? 0L : cardBenefit.getLowerCategory().getBenefitCommonId();
        return new CardBenefitResponse(
                cardBenefit.getBenefitId(),
                cardBenefit.getBenefitRate(),
                cardBenefit.getIsActive(),
                cardBenefit.getCardDetail().getCardSequenceId(),
                cardBenefit.getUpperCategory().getBenefitCommonId(),
                cardBenefitLowerId,
                cardBenefit.getCreatedAt(),
                cardBenefit.getModifiedAt()
        );
    }
}