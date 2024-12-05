package com.quostomize.quostomize_be.domain.log.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class MDCLoggingFilter implements Filter {

    private static final String TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUri = httpRequest.getRequestURI();

        // auth 경로일 때만 MDC 처리
        if (requestUri.startsWith("/v1/api/auth") ||
                requestUri.startsWith("/v1/api/member") ||
                requestUri.startsWith("/v1/api/benefit-change") ||
                requestUri.startsWith("/v1/api/card-applicants") ||
                requestUri.startsWith("/v1/api/admin/email")
        ) {
            String traceId = httpRequest.getHeader(TRACE_ID);
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString();
            }

            MDC.put(TRACE_ID, traceId);
            MDC.put("requestUri", requestUri);
            MDC.put("clientIp", request.getRemoteAddr());
            log.info("[사용자 traceId: {}]", traceId);
            log.info("[사용자 clientIp: {}]", MDC.get("clientIp"));
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
