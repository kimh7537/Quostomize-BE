package com.quostomize.quostomize_be.api.memberQuestion.dto;

import java.time.LocalDateTime;

public record PageMemberQuestionResponse(
        Long questionSequenceId,
        Boolean isPrivate,
        Boolean isAnswered,
        Long categoryCode,
        String questionTitle,
        LocalDateTime createdAt
) {
}
