package com.quostomize.quostomize_be.common.error.exception;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;
}
