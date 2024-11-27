package com.quostomize.quostomize_be.domain.customizer.card.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardStatus {
    CREARTION_PENDING,
    ACTIVE,
    CANCELLATION_PENDING,
    CANCELLED;
}
