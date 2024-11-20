package com.quostomize.quostomize_be.api.stock.dto;

import jakarta.validation.constraints.NotNull;

public record StockInterestRequestDto(
        @NotNull
        int currentOrder,
        @NotNull
        int editOrder
) {
    public StockInterestRequestDto(@NotNull
                                  int currentOrder, @NotNull
                                  int editOrder) {
        this.currentOrder = currentOrder;
        this.editOrder = editOrder;
    }
}
