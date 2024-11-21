package com.quostomize.quostomize_be.common.error.handler;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.error.response.ErrorResponse;
import com.quostomize.quostomize_be.common.error.response.MethodArgumentErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static com.quostomize.quostomize_be.common.error.response.MethodArgumentErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppCustomException(AppException e,
                                                                  HttpServletRequest request) {
        log.error("AppException 발생: {}", e.getErrorCode().getMessage());
        log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
        String requestURI = httpServletRequest.getRequestURI();

        List<FieldErrorResponse> fieldErrorResponses = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldErrorResponse::of)
                .toList();

        List<String> errorFields = fieldErrorResponses.stream()
                .map(FieldErrorResponse::getField)
                .toList();

        log.error("[Method Argument Not Valid Execption 발생]: {}", errorFields);
        log.error("에러가 발생한 지점 {}, {}", httpServletRequest.getMethod(), requestURI);

        MethodArgumentErrorResponse errorResponse = of(ErrorCode.INVALID_INPUT, httpServletRequest, fieldErrorResponses);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e,
                                                                       HttpServletRequest request) {
        log.error("Entity Not Found Exception 발생: {}", e.getMessage());
        log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e,
                                                                        HttpServletRequest request) {
        log.error("Illegal Argument Exception 발생: {}", e.getMessage());
        log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_MATCHED_PASSWORD, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException e,
                                                                   HttpServletRequest request) {
        log.error("RestClient Exception 발생: {}", e.getMessage());
        log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.OPENAPI_CONNECT_FAIL, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // 서버 내부 예외 처리 핸들러
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(RuntimeException e, HttpServletRequest request) {
        log.error("RuntimeException 발생: {}", e.getMessage());
        log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Exception 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, HttpServletRequest request) {
        log.error("Exception 발생: {}", e.getMessage());
        log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.GENERAL_ERROR, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}