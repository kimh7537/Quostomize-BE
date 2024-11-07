package com.quostomize.quostomize_be.api.payment.dto;

public record Product(
        String name,
        String price,
        String amount,
        String totalPrice
) {
}
