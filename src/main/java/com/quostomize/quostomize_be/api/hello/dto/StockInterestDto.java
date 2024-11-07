package com.quostomize.quostomize_be.api.hello.dto;

public record StockInterestDto (
        Integer priority,
        String stockName,
        Integer stockPresentPrice
) {
}
