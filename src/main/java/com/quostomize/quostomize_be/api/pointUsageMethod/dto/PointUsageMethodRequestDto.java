package com.quostomize.quostomize_be.api.pointUsageMethod.dto;


import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;

public record PointUsageMethodRequestDto(
        Long pointUsageTypeId,
        CardDetail cardDetail,
        Boolean isLotto,
        Boolean isActive,
        String usageType
) {
    public static PointUsageMethodRequestDto from(PointUsageMethod pointUsageMethod) {
        boolean isLotto = pointUsageMethod.getIsLotto();

        String usageType = pointUsageMethod.getIsPayback() ? "payback" :
                pointUsageMethod.getIsPieceStock() ? "piecestock" : null;

        boolean isActive = pointUsageMethod.getIsPayback() || pointUsageMethod.getIsPieceStock();

        return new PointUsageMethodRequestDto(
                pointUsageMethod.getPointUsageTypeId(),
                pointUsageMethod.getCardDetail(),
                isLotto,
                isActive,
                usageType
        );
    }

    public PointUsageMethod toEntity() {
        return PointUsageMethod.builder()
                .pointUsageTypeId(this.pointUsageTypeId())
                .cardDetail(this.cardDetail())
                .isLotto(this.isLotto())
                .isPayback(this.isActive())
                .isPieceStock(this.isActive())
                .build();
    }
}