package com.quostomize.quostomize_be.common.error.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quostomize.quostomize_be.common.error.ErrorCode;

public class JsonProcessingAppException extends AppException {

    public JsonProcessingAppException(JsonProcessingException cause) {
        super(ErrorCode.JSON_PROCESSING_ERROR, cause);
    }
}
