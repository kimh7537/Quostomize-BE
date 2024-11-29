package com.quostomize.quostomize_be.api.admin.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageAdminResponse<T>(
        List<T> content,
        int currentPage,
        int totalPage
) {
    public PageAdminResponse(Page<T> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages()
        );
    }
}