package com.quostomize.quostomize_be.api.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockInterestDto (
        @NotNull
        Integer priority,
        @NotNull
        @NotBlank
        String stockName,
        @NotNull
        Integer stockPresentPrice,
        @NotNull
        @NotBlank
        String stockImage
) {
    public StockInterestDto withStockImage(String newStockImage) {
        return new StockInterestDto(priority, stockName, stockPresentPrice, newStockImage);
    }
}