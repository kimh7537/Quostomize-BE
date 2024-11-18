package com.quostomize.quostomize_be.api.point.dto;

import com.quostomize.quostomize_be.domain.customizer.point.entity.PointUsageMethod;

public record PointUsageMethodResponseDto(
        Long pointUsageTypeId,
        Boolean isLotto,
        Boolean isPayback,
        Boolean isPieceStock
) {
    public static PointUsageMethodResponseDto from(PointUsageMethod pointUsageMethod) {
        return new PointUsageMethodResponseDto(
                pointUsageMethod.getPointUsageTypeId(),
                pointUsageMethod.getIsLotto(),
                pointUsageMethod.getIsPayback(),
                pointUsageMethod.getIsPieceStock()
        );
    }
}
