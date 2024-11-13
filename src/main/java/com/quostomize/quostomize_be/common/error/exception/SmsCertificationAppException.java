package com.quostomize.quostomize_be.common.error.exception;

import com.quostomize.quostomize_be.common.error.ErrorCode;

public class SmsCertificationAppException extends AppException {

    public SmsCertificationAppException(String message) {
        super(ErrorCode.SMS_CERTIFICATION_ERROR, new RuntimeException(message));
    }
}


