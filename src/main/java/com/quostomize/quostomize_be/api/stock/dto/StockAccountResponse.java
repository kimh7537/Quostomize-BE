package com.quostomize.quostomize_be.api.stock.dto;

import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockAccount;

public record StockAccountResponse(
        Long stockAccountId,
        Long stockAccountNumber,
        String stockAccountName,
        Long customerId
) {

    public static StockAccountResponse from(StockAccount stockAccount){
        return new StockAccountResponse(
                stockAccount.getStockAccountId(),
                stockAccount.getStockAccountNumber(),
                stockAccount.getStockAccountName(),
                stockAccount.getCustomer().getCustomerId()
        );
    }
}
