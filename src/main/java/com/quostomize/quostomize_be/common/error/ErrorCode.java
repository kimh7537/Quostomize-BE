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
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "A-102", "비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A-201", "해당 이메일이 존재하지 않습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "A-301", "존재하는 이메일 입니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "A-302", "존재하는 전화번호입니다."),

    // 고객 관련 에러
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "C-201", "고객 정보를 찾을 수 없습니다."),
    CUSTOMER_ALREADY_EXISTS(HttpStatus.CONFLICT, "C-202", "이미 존재하는 고객입니다."),

    // 로또 관련 에러
    LOTTO_PARTICIPANT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "L-301", "이미 로또에 참여한 고객입니다."),
    LOTTO_PARTICIPATION_FAILED(HttpStatus.BAD_REQUEST, "L-401", "로또 참여 조건을 충족하지 못했습니다."),
    INSUFFICIENT_LOTTO_POINTS(HttpStatus.BAD_REQUEST, "L-402", "로또 참여에 필요한 포인트가 부족합니다."),

    // 포인트 관련 에러
    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "P-201", "포인트가 부족합니다."),
    POINT_USAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "P-202", "포인트 사용 한도를 초과했습니다."),

    // 나의 카드 P
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "M-001", "카드 정보가 없습니다,"),
    PAYBACK_AND_PIECESTOCK_CONFLICT(HttpStatus.BAD_REQUEST, "M-002", "페이백과 조각투자는 동시에 활성화 할 수 없습니다."),
    MINIMUM_ONE_OPTION_REQUIRED(HttpStatus.BAD_REQUEST, "M-101", "최소 한 가지 이상의 옵션을 선택하세요."),
    MAXIMUM_TWO_OPTIONS_ALLOWED(HttpStatus.BAD_REQUEST, "M-102", "최대 두 가지의 옵션을 선택할 수 있습니다."),

    // 주식
    OPENAPI_CONNECT_FAIL(HttpStatus.BAD_REQUEST, "S-001", "오픈 api 테스트가 실패했습니다."),

    //JSON 처리 관련 에러
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "J-001", "JSON 처리 중 오류가 발생했습니다."),

    // SMS 인증
    SMS_CERTIFICATION_ERROR(HttpStatus.BAD_REQUEST, "M-001", "인증번호가 일치하지 않습니다."),
    SMS_CERTIFICATION_EXPIRED(HttpStatus.BAD_REQUEST, "M-002", "인증번호가 만료되었습니다."),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "M-003", "유효하지 않은 전화번호 형식입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}