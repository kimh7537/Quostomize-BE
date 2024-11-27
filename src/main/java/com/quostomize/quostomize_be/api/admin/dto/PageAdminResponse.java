package com.quostomize.quostomize_be.api.admin.dto;

import com.quostomize.quostomize_be.api.card.dto.CardDetailResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageAdminResponse(
        List<CardDetailResponse> content,
        int currentPage,
        int totalPage
) {
    public PageAdminResponse(Page<CardDetailResponse> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages()
        );
    }
}