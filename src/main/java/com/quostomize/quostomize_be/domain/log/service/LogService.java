package com.quostomize.quostomize_be.domain.log.service;

import com.quostomize.quostomize_be.domain.log.entity.SystemLog;
import com.quostomize.quostomize_be.domain.log.enums.LogStatus;
import com.quostomize.quostomize_be.domain.log.enums.LogType;
import com.quostomize.quostomize_be.domain.log.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LogService {

    private final SystemLogRepository systemLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(LogType logType, String message, Long memberId, LogStatus status, String requestUri) {
        // traceId 가져오기
        String traceId = MDC.get("traceId");

        // traceId가 null인 경우 생성
        if (traceId == null || traceId.equals("")) {
            traceId = "NO_TRACE_ID"; // 필요 시 UUID.randomUUID().toString()로 대체 가능
            log.warn("traceId가 null이므로 기본값 사용: {}", traceId);
        }

        // 로그 엔티티 생성 및 저장
        SystemLog logEntity = SystemLog.builder()
                .traceId(traceId)
                .logType(logType)
                .message(message)
                .memberId(memberId)
                .status(status)
                .requestUri(requestUri)
                .build();

        // DB에 로그 저장
        SystemLog savedLog = systemLogRepository.save(logEntity);
        log.info("로그 저장 확인: {}", savedLog);

        // 로그를 추가로 기록 (옵션)
        log.info("로그 저장 완료: logType: {}, memberId: {}, status: {}, traceId: {}", logType, memberId, status, traceId);
    }
}
