package com.quostomize.quostomize_be.api.pointUsageMethod.dto;

public record PointUsageMethodResponse(
        Long pointUsageTypeId,
        Boolean isLotto,
        Boolean isPayback,
        Boolean isPieceStock,
        Long cardSequenceId,
        Integer cardColor,
        String cardNumber,
        Long benefitId,
        Integer benefitRate,
        Boolean isActive,
        String upperCategoryType,
        String lowerCategoryType,
        String franchiseName
) {}
