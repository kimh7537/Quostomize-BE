package com.quostomize.quostomize_be.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class MDCLoggingFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 traceId 가져오기 (없을 경우 새로 생성)
        String traceId = request.getHeader(TRACE_ID);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        // MDC에 traceId 설정
        MDC.put(TRACE_ID, traceId);
        // 추가 정보 설정 (예: 요청 URI, 요청자 IP 등)
        MDC.put("requestUri", request.getRequestURI());
        MDC.put("clientIp", request.getRemoteAddr());

        try {
            filterChain.doFilter(request, response);
        } finally {
            // MDC 클리어
            MDC.clear();
        }
    }
}
