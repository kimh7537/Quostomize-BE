package com.quostomize.quostomize_be.api.cardapplicant.dto;

public record BenefitHierarchyDTO(
        Long benefitCommonId,
        String benefitCategoryType,
        Long parentCodeId,
        String franchiseName
) {}