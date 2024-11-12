package com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto;


import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;

public record PointUsageMethodRequestDto(
        Long pointUsageTypeId,
        CardDetail cardDetail,
        Boolean isLotto,
        Boolean isPayback,
        Boolean isPieceStock
) {
    public static PointUsageMethodRequestDto from(PointUsageMethod pointUsageMethod) {
        return new PointUsageMethodRequestDto(
                pointUsageMethod.getPointUsageTypeId(),
                pointUsageMethod.getCardDetail(),
                pointUsageMethod.getIsLotto(),
                pointUsageMethod.getIsPayback(),
                pointUsageMethod.getIsPieceStock()
        );
    }

    public PointUsageMethod toEntity() {
        return PointUsageMethod.builder()
                .pointUsageTypeId(this.pointUsageTypeId())
                .cardDetail(this.cardDetail())
                .isLotto(this.isLotto())
                .isPayback(this.isPayback())
                .isPieceStock(this.isPieceStock())
                .build();
    }
}