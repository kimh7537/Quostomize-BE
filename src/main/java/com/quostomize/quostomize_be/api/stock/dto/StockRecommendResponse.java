package com.quostomize.quostomize_be.api.stock.dto;

public record StockRecommendResponse(
        String stockName,
        Integer stockPresentPrice,
        String stockImage
) {
    public StockRecommendResponse(String stockName, Integer stockPresentPrice, String stockImage) {
        this.stockName = stockName;
        this.stockPresentPrice = stockPresentPrice;
        this.stockImage = stockImage;
    }
}
