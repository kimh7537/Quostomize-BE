package com.quostomize.quostomize_be.api.pointUsageMethod.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LottoRequestDto(
        @Positive Long pointUsageTypeId,
        @NotNull Long cardSequenceId,
        @NotNull Boolean isLotto
) { }