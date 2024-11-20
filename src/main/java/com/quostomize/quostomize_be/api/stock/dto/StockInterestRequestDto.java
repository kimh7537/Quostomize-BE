package com.quostomize.quostomize_be.api.stock.dto;

import jakarta.validation.constraints.NotNull;

public record StockInterestRequestDto(
        @NotNull
        int currentOrder,
        @NotNull
        int editOrder,
        @NotNull
        Long cardId
) {
    public StockInterestRequestDto(@NotNull
                                   int currentOrder, @NotNull
                                   int editOrder, @NotNull
                                   Long cardId) {
        this.currentOrder = currentOrder;
        this.editOrder = editOrder;
        this.cardId = cardId;
    }
}
