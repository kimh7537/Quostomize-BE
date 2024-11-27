package com.quostomize.quostomize_be.api.memberQuestion.dto;

import java.util.List;

public record PageResponse(
        List<PageMemberQuestionResponse> content, // 현재 페이지에 해당하는 질문 데이터 리스트
        int currentPage,
        int totalPages,
        int pageSize, // 페이지당 데이터 수
        long totalElements // 전체 데이터 수
) {
}
