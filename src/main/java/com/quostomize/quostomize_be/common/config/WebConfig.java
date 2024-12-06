package com.quostomize.quostomize_be.common.config;

import com.quostomize.quostomize_be.common.idempotency.IdempotencyInterceptor;
import com.quostomize.quostomize_be.common.idempotency.IdempotencyRedisRepository;
import com.quostomize.quostomize_be.domain.log.filter.MDCLoggingFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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

//    @Bean
//    public FilterRegistrationBean<MDCLoggingFilter> logFilter() {
//        FilterRegistrationBean<MDCLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new MDCLoggingFilter());
//        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        filterRegistrationBean.addUrlPatterns("/v1/api/auth/*");
//        return filterRegistrationBean;
//    }
}

