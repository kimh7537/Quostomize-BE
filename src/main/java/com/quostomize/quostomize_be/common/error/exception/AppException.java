package com.quostomize.quostomize_be.common.error.exception;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import lombok.Getter;


@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}