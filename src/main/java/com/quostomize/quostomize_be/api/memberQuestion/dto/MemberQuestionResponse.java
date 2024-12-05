package com.quostomize.quostomize_be.api.memberQuestion.dto;

import com.quostomize.quostomize_be.domain.auth.entity.Member;

import java.time.LocalDateTime;

public record MemberQuestionResponse(
        Long questionSequenceId,
        Boolean isPrivate,
        Boolean isAnswered,
        Long categoryCode,
        String questionTitle,
        String questionContent,
        Long memberId,
        LocalDateTime createdAt
) {
}