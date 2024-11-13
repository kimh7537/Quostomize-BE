package com.quostomize.quostomize_be.common.error.exception;

import com.quostomize.quostomize_be.common.error.ErrorCode;

public class InvalidPhoneNumberException extends AppException {

    public InvalidPhoneNumberException() {
        super(ErrorCode.INVALID_PHONE_FORMAT, new RuntimeException("유효하지 않은 전화번호 형식입니다."));
    }
}