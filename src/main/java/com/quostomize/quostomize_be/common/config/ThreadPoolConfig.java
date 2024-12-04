package com.quostomize.quostomize_be.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        int processors = Runtime.getRuntime().availableProcessors(); // CPU 코어 수
        int corePoolSize = Math.max(2, processors); // 최소 스레드: 2 또는 CPU 코어 수
        int maxPoolSize = corePoolSize * 2; // 최대 스레드: 최소 스레드의 2배

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize); // 최소 스레드 수
        executor.setMaxPoolSize(maxPoolSize); // 최대 스레드 수
        executor.setQueueCapacity(100); // 대기열 크기
        executor.setThreadNamePrefix("EmailSender-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}

