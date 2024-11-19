package com.quostomize.quostomize_be.common.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterAuthenticationException extends RuntimeException {

    private String message;
}
