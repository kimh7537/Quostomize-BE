package com.quostomize.quostomize_be.api.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockRecommendResponse(
        @NotNull
        @NotBlank
        String stockName,
        @NotNull
        Integer stockPresentPrice,
        @NotNull
        @NotBlank
        String stockImage
) {
    public StockRecommendResponse(String stockName, Integer stockPresentPrice, String stockImage) {
        this.stockName = stockName;
        this.stockPresentPrice = stockPresentPrice;
        this.stockImage = stockImage;
    }
}
