package com.quostomize.quostomize_be.common.config;

import com.quostomize.quostomize_be.common.idempotency.IdempotencyInterceptor;
import com.quostomize.quostomize_be.common.idempotency.IdempotencyRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final IdempotencyRedisRepository idempotencyRedisRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IdempotencyInterceptor(idempotencyRedisRepository))
                .addPathPatterns("/**")
                .order(1);
    }
}

