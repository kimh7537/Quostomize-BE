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

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String traceId = httpServletRequest.getHeader(TRACE_ID);

        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        MDC.put(TRACE_ID, traceId);
        MDC.put("requestUri", httpServletRequest.getRequestURI());
        MDC.put("clientIp", request.getRemoteAddr());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
