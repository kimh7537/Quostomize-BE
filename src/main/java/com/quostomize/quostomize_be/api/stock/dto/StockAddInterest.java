package com.quostomize.quostomize_be.api.stock.dto;

import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInterest;

import java.util.List;

public record StockAddInterest(
        Customer customer,
        long stockInterestCount
) {
    public static StockAddInterest from(Customer customer, long stockInterestCount){
        return new StockAddInterest(
                customer,
                stockInterestCount
        );
    }
}
