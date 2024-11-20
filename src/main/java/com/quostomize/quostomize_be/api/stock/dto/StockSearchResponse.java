package com.quostomize.quostomize_be.api.stock.dto;

public record StockSearchResponse(
        Long stockInformationId,
        Integer stockCode,
        String stockName,
        Integer stockPresentPrice,
        String stockImage
) {
}
