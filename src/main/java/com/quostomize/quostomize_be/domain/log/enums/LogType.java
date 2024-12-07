package com.quostomize.quostomize_be.domain.log.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogType {
    // 로그인/로그아웃 관련
    LOGIN_ATTEMPT("로그인 시도"),
    LOGIN_SUCCESS("로그인 성공"),
    LOGIN_FAILURE("로그인 실패"),
    LOGOUT("로그아웃"),

    // 메일 발송 관련
    MAIL_SEND("메일 발송 성공"),
    MAIL_FAILURE("메일 발송 실패"),

    // 결제 관련
    PAYMENT_REQUEST("결제 요청"),
    PAYMENT_SUCCESS("결제 성공"),
    PAYMENT_FAILURE("결제 실패"),

    // API 호출 관련
    API_CALL("API 호출"),
    API_SUCCESS("API 호출 성공"),
    API_FAILURE("API 호출 실패"),

    // 에러 로그 관련
    EXCEPTION("에러 발생"),
    VALIDATION_ERROR("유효성 검사 에러");

    private final String description;
}
