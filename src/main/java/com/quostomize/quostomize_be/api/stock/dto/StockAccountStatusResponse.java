package com.quostomize.quostomize_be.api.stock.dto;

import com.quostomize.quostomize_be.domain.customizer.stock.enums.PageType;

public record StockAccountStatusResponse<T>(
        PageType pageType,
        T accounts
) {
    public StockAccountStatusResponse(PageType pageType, T accounts) {
        this.pageType = pageType;
        this.accounts = accounts;
    }
}
