package com.quostomize.quostomize_be.common.idempotency;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessStatus {


    PROCESSING("현재 해당 요청 처리 중"),
    SUCCESS("요청 완료"),
    FAILED("요청 실패")

    ;

    private final String description;
}
