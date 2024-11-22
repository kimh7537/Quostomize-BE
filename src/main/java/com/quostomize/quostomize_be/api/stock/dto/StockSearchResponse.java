package com.quostomize.quostomize_be.api.stock.dto;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;

public record StockSearchResponse(
        Long stockInformationId,
        Integer stockCode,
        String stockName,
        Integer stockPresentPrice,
        String stockImage
) {

    public static StockSearchResponse from(StockInformation stockInformation){
        return new StockSearchResponse(
                stockInformation.getStockInformationId(),
                stockInformation.getStockCode(),
                stockInformation.getStockName(),
                stockInformation.getStockPresentPrice(),
                stockInformation.getStockImage()
        );
    }

}
