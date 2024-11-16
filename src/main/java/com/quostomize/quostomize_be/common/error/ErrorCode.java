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
    LOGIN_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "A-201", "해당 아이디가 존재하지 않습니다."),
    PHONE_NOT_FOUND(HttpStatus.NOT_FOUND, "A-202", "해당 번호가 존재하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A-203", "해당 이메일이 존재하지 않습니다."),
    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT, "A-301", "존재하는 아이디 입니다."),
    PHONE_DUPLICATED(HttpStatus.CONFLICT, "A-302", "존재하는 핸드폰 번호 입니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "A-303", "존재하는 이메일 입니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "A-304", "존재하는 전화번호입니다."),

    //로그인
    // Auth 일반적인 인증 문제 Auth JWT 토큰 관련 에러
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "T-001", "이미 만료된 토큰입니다."),
    FILTER_EXCEPTION(HttpStatus.UNAUTHORIZED, "T-002", "필터 내부에러 발생"),
    JWT_FORM_ERROR(HttpStatus.UNAUTHORIZED, "T-003", "jwt 형식 에러 발생"),
    REFRESH_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "T-004", "해당 리프레시 토큰이 DB에 존재하지 않습니다."),
    REISSUE_FAIL(HttpStatus.UNAUTHORIZED, "T-005", "액세스 토큰 재발급 요청 실패"),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "T-006", "로그인에 실패했습니다."),

    //회원 관련
    ROLE_IS_NOT_MATCH(HttpStatus.BAD_REQUEST, "M-101", "해당 ROLE은 변경할 수 없습니다."),
    ROLE_IS_NOT_OLD_MEMBER(HttpStatus.BAD_REQUEST, "M-103", "해당 회원의 ROLE은 OLD_MEMBER가 아닙니다."),
    SAME_PASSWORD(HttpStatus.CONFLICT, "M-301", "이전과 같은 비밀번호로 변경할 수 없습니다."),

    // 상위 예외 처리
    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G-001", "예상치 못한 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G-002", "서버 내부 오류가 발생했습니다."),

    // 카드
    CARD_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "C-201", "해당 카드 정보를 찾을 수 없습니다."),
    
    // 카드 혜택
    CARD_DETAIL_BENEFIT_NOT_FOUND(HttpStatus.NOT_FOUND, "B-201", "해당 카드에 적용된 혜택이 없습니다."),
    CARD_BENEFIT_RESERVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B-501", "예약한 혜택 반영에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
