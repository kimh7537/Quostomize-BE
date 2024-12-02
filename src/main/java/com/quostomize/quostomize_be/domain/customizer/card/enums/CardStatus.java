package com.quostomize.quostomize_be.domain.customizer.card.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum CardStatus {
    CREATION_PENDING("CREATION_PENDING", "카드 신청 대기") {
        @Override
        public boolean transitionTo(CardStatus newStatus) {
            return newStatus == ACTIVE || newStatus == CANCELLED;
        }
    },
    ACTIVE("ACTIVE", "카드 이용 가능") {
        @Override
        public boolean transitionTo(CardStatus newStatus) {
            return newStatus == CANCELLED || newStatus == CANCELLATION_PENDING;
        }
    },
    CANCELLATION_PENDING("CANCELLATION_PENDING", "카드 해지 대기") {
        @Override
        public boolean transitionTo(CardStatus newStatus) {
            return newStatus == CANCELLED;
        }
    },
    CANCELLED("CANCELLED", "카드 해지") {
        @Override
        public boolean transitionTo(CardStatus newStatus) {
            return false;
        }
    };

    private static final Map<String, CardStatus> STATUS_KEY_MAP = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(CardStatus::getKey, Function.identity()));

    private final String key;
    private final String description;

    public abstract boolean transitionTo(CardStatus newStatus);

    public static CardStatus fromKey(final String key) {
        CardStatus status = STATUS_KEY_MAP.get(key);
        if (status == null) {
            throw new IllegalArgumentException(String.format("요청한 key(%s)를 찾을 수 없습니다.", key));
        }
        return status;
    }
}