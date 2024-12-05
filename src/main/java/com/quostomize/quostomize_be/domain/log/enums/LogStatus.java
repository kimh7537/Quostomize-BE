package com.quostomize.quostomize_be.domain.log.enums;

public enum LogStatus {
    TRACE,     // 상세 정보 추적용
    DEBUG,     // 디버그용 정보
    INFO,      // 정보성 메시지
    WARN,      // 경고 상황
    ERROR,     // 에러 발생
    FATAL,     // 치명적인 오류
    SUCCESS,   // 작업 성공
    FAILURE    // 작업 실패
}
