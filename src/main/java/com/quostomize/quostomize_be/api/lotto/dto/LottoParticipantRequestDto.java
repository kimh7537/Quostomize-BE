package com.quostomize.quostomize_be.api.lotto.dto;

import jakarta.validation.constraints.NotNull;

public record LottoParticipantRequestDto(
        @NotNull Long cardSequenceId,
        @NotNull Boolean isLottoOn
) {
}