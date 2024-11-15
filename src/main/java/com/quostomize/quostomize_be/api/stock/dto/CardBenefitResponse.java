package com.quostomize.quostomize_be.api.stock.dto;

public record CardBenefitResponse(
     Boolean isActive, // 활성화여부
     Integer benefitRate, // 혜택률
     String benefitCategoryType, // 상위분류
     String franchiseName // 하위분류

) {
    public CardBenefitResponse(Boolean isActive, Integer benefitRate, String benefitCategoryType, String franchiseName) {
        this.isActive = isActive;
        this.benefitRate = benefitRate;
        this.benefitCategoryType = benefitCategoryType;
        this.franchiseName = franchiseName;
    }
}
