package com.quostomize.quostomize_be.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // DTO 에서 발생하는 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "I-001", "입력 값이 잘못되었습니다."),
    // 404 오류 -> 객체를 찾을 수 없는 문제
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "I-201", "해당 Entity를 찾을 수 없습니다."),

    // 회원 가입
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "A-002", "유효하지 않은 패스워드입니다."),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "A-003", "유효하지 않은 전화번호 입니다."),
    CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "A-101", "요청하신 코드가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A-201", "해당 이메일이 존재하지 않습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "A-301", "존재하는 이메일 입니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "A-302", "존재하는 전화번호입니다."),

    // 주식
    OPENAPI_CONNECT_FAIL(HttpStatus.BAD_REQUEST, "S-001", "오픈 api 테스트가 실패했습니다."),

    //JSON 처리 관련 에러
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "J-001", "JSON 처리 중 오류가 발생했습니다."),

    // SMS 인증
    SMS_CERTIFICATION_ERROR(HttpStatus.BAD_REQUEST, "M-001", "인증번호가 일치하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}