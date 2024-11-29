package com.quostomize.quostomize_be.api.memberQuestion.dto;

public record MemberQuestionRequest(
        Boolean isPrivate,
        Long categoryCode,
        String questionTitle,
        String questionContent
) {
}
