package com.quostomize.quostomize_be.common.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";
    private final IdempotencyRedisRepository idempotencyRedisRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);
        log.info("[idempotency key: {}]", idempotencyKey);
        if (idempotencyKey == null) {
            return true;
        }
        ObjectMapper objectMapper = new ObjectMapper();

        if (idempotencyRedisRepository.hasSucceedResult(idempotencyKey)) {
            response.getWriter()
                    .write(objectMapper.writeValueAsString(
                            idempotencyRedisRepository.getSucceedResponse(idempotencyKey)));
            log.info("[멱등성 DB에 데이터 존재]");
            return false;
        }

        if (idempotencyRedisRepository.isProcessing(idempotencyKey)) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter()
                    .write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.PROCESSING, request)));
            log.warn("[요청은 왔지만 아직 처리 중]");
            return false;
        }

        if (idempotencyRedisRepository.canRetry(idempotencyKey)) {
            log.info("[재시도 가능 상태. 처리 진행]");
            idempotencyRedisRepository.saveStatusProcessing(idempotencyKey);
            return true;
        }

        // 캐시에 결과가 존재하지 않으면 -> 처리중이란 값을 넣고 컨트롤러를 실행함
        idempotencyRedisRepository.saveStatusProcessing(idempotencyKey);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);
        if (idempotencyKey == null) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();

        if (ex != null || response.getStatus() >= 400) {
            // 요청 처리 중 예외 또는 오류 발생
            log.warn("[afterCompletion] 요청 처리 실패. FAILED 상태로 전환. idempotencyKey: {}", idempotencyKey);
            idempotencyRedisRepository.saveFailedStatus(idempotencyKey); // FAILED 상태로 저장
        } else {
            // 요청 성공
            final ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            idempotencyRedisRepository.saveSucceedResult(idempotencyKey,
                    objectMapper.readTree(responseWrapper.getContentAsByteArray()));
            responseWrapper.copyBodyToResponse();
            log.info("[afterCompletion] 요청 처리 성공. SUCCESS 상태로 전환. idempotencyKey: {}", idempotencyKey);
        }
    }
}
