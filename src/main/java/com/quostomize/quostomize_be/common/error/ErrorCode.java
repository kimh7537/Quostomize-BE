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


    // 나의 카드 P
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "M-001", "카드 정보가 없습니다,"),
    PAYBACK_AND_PIECESTOCK_CONFLICT(HttpStatus.BAD_REQUEST, "M-002", "페이백과 조각투자는 동시에 활성화 할 수 없습니다."),
    MINIMUM_ONE_OPTION_REQUIRED(HttpStatus.BAD_REQUEST, "M-101", "최소 한 가지 이상의 옵션을 선택하세요."),
    MAXIMUM_TWO_OPTIONS_ALLOWED(HttpStatus.BAD_REQUEST, "M-102", "최대 두 가지의 옵션을 선택할 수 있습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
