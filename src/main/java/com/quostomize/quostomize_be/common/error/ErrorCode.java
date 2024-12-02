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
    INVALID_LOGIN_ID(HttpStatus.BAD_REQUEST, "A-001","유효하지 않은 아이디입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "A-002", "유효하지 않은 패스워드입니다."),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "A-003", "유효하지 않은 전화번호 입니다."),
    CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "A-101", "요청하신 코드가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "A-102", "비밀번호가 일치하지 않습니다."),
    NOT_MATCHED_SECONDARY_AUTH_CODE(HttpStatus.BAD_REQUEST, "A-401", "인증번호가 일치하지 않습니다."),
    NOT_MATCHED_PASSWORD(HttpStatus.BAD_REQUEST, "A-402", "비밀번호가 일치하지 않습니다."),
    LOGIN_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "A-201", "해당 아이디가 존재하지 않습니다."),
    PHONE_NOT_FOUND(HttpStatus.NOT_FOUND, "A-202", "해당 번호가 존재하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A-203", "해당 이메일이 존재하지 않습니다."),
    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT, "A-301", "존재하는 아이디 입니다."),
    PHONE_DUPLICATED(HttpStatus.CONFLICT, "A-302", "존재하는 핸드폰 번호 입니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "A-303", "존재하는 이메일 입니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "A-304", "존재하는 전화번호입니다."),
    RESIDENCE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "A-305", "존재하는 주민번호입니다."),

    //로그인
    // Auth 일반적인 인증 문제 Auth JWT 토큰 관련 에러
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "T-001", "이미 만료된 토큰입니다."),
    FILTER_EXCEPTION(HttpStatus.UNAUTHORIZED, "T-002", "필터 내부에러 발생"),
    JWT_FORM_ERROR(HttpStatus.UNAUTHORIZED, "T-003", "jwt 형식 에러 발생"),
    REFRESH_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "T-004", "해당 리프레시 토큰이 DB에 존재하지 않습니다."),
    REISSUE_FAIL(HttpStatus.UNAUTHORIZED, "T-005", "액세스 토큰 재발급 요청 실패"),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "T-006", "로그인에 실패했습니다."),
    MEMBER_INFO_NOT_FOUND(HttpStatus.UNAUTHORIZED, "T-007", "로그인된 사용자 정보를 가져올 수 없습니다."),

    //회원 관련
    ROLE_IS_NOT_MATCH(HttpStatus.BAD_REQUEST, "M-101", "해당 ROLE은 변경할 수 없습니다."),
    ROLE_IS_NOT_OLD_MEMBER(HttpStatus.BAD_REQUEST, "M-103", "해당 회원의 ROLE은 OLD_MEMBER가 아닙니다."),
    SAME_PASSWORD(HttpStatus.CONFLICT, "M-301", "이전과 같은 비밀번호로 변경할 수 없습니다."),
    USER_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "M-401", "권한이 없어서 접근할 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M-404", "해당 MEMBER를 찾을 수 없습니다."),

    // 고객 관련
    CUSTOMER_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "U-001", "고객의 카드를 찾을 수 없습니다."),
    CUSTOMER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U-002", "이미 존재하는 고객입니다."),

    // 상위 예외 처리
    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G-001", "예상치 못한 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G-002", "서버 내부 오류가 발생했습니다."),

    PROCESSING(HttpStatus.CONFLICT, "D-999", "해당 키의 요청은 아직 처리 중 입니다."),

    // 카드
    CARD_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "C-201", "해당 카드 정보를 찾을 수 없습니다."),
    CARD_STATUS_CHANGE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "C-202", "카드 상태를 변경할 수 없습니다."),

    // 카드 혜택
    CARD_DETAIL_BENEFIT_NOT_FOUND(HttpStatus.NOT_FOUND, "B-201", "해당 카드에 적용된 혜택이 없습니다."),
    CARD_BENEFIT_RESERVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B-501", "예약한 혜택 반영에 실패했습니다."),
    SECONDARY_AUTH_CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "B-001", "2차 비밀번호가 일치하지 않습니다."),

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
    MORE_THAN_LIMIT(HttpStatus.BAD_REQUEST, "S-002", "위시리스트에 최대 3개의 종목만 추가할 수 있습니다."),
    //JSON 처리 관련 에러
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "J-001", "JSON 처리 중 오류가 발생했습니다."),

    // SMS 인증
    SMS_CERTIFICATION_ERROR(HttpStatus.BAD_REQUEST, "M-001", "인증번호가 일치하지 않습니다."),
    SMS_CERTIFICATION_EXPIRED(HttpStatus.BAD_REQUEST, "M-002", "인증번호가 만료되었습니다."),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "M-003", "유효하지 않은 전화번호 형식입니다."),

    // QnA
    DUPLICATE_REQUEST(HttpStatus.ALREADY_REPORTED, "Q-101", "이미 답변이 등록된 문의글입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
