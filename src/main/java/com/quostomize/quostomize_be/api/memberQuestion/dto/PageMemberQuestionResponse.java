package com.quostomize.quostomize_be.api.memberQuestion.dto;

public record PageMemberQuestionResponse(
        Long questionSequenceId,
        Boolean isPrivate,
        Boolean isAnswered,
        Long categoryCode,
        String questionTitle
) {
}
