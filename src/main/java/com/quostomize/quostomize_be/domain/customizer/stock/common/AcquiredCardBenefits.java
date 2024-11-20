package com.quostomize.quostomize_be.domain.customizer.stock.common;

import com.quostomize.quostomize_be.api.stock.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;


import java.util.List;
import java.util.stream.Collectors;

public class AcquiredCardBenefits {

    public List<CardBenefitResponse> getCardBenefitDto(List<CardBenefit> cardBenefits){

        List<CardBenefitResponse> benefits = cardBenefits.stream()
                .map(Benefit -> {
                    String benefitCategoryType = Benefit.getUpperCategory().getBenefitCategoryType();
                    String franchiseName = Benefit.getLowerCategory() != null
                            ? Benefit.getLowerCategory().getBenefitCategoryType()
                            : null; // 하위타입이 없으면 null을 넣는다
                    return new CardBenefitResponse(
                            Benefit.getIsActive(),
                            Benefit.getBenefitRate(),
                            benefitCategoryType,
                            franchiseName
                    );
                }).collect(Collectors.toList());

        return benefits;
    }
}
