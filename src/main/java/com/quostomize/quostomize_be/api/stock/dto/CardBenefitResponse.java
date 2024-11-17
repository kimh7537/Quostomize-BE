package com.quostomize.quostomize_be.api.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardBenefitResponse(
     @NotNull
     Boolean isActive, // 활성화여부
     @NotNull
     Integer benefitRate, // 혜택률
     @NotNull
     @NotBlank
     String benefitCategoryType, // 상위분류
     @NotNull
     @NotBlank
     String franchiseName // 하위분류

) {
    public CardBenefitResponse(Boolean isActive, Integer benefitRate, String benefitCategoryType, String franchiseName) {
        this.isActive = isActive;
        this.benefitRate = benefitRate;
        this.benefitCategoryType = benefitCategoryType;
        this.franchiseName = franchiseName;
    }
}
