package com.quostomize.quostomize_be.api.card.dto;

import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;

public record CardStatusRequest(
        Long adminId,
        Long cardSequenceId,
        CardStatus status,
        String secondaryAuthCode
) { }