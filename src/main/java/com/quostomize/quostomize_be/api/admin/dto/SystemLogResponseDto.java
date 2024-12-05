package com.quostomize.quostomize_be.api.admin.dto;

import com.quostomize.quostomize_be.domain.log.entity.SystemLog;
import com.quostomize.quostomize_be.domain.log.enums.LogStatus;
import com.quostomize.quostomize_be.domain.log.enums.LogType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SystemLogResponseDto {

    private Long logId;
    private String traceId;
    private LogType logType;
    private String message;
    private Long memberId;
    private LogStatus status;
    private String requestUri;
    private LocalDateTime createdAt;

    public static SystemLogResponseDto fromEntity(SystemLog systemLog) {
        return new SystemLogResponseDto(
                systemLog.getLogId(),
                systemLog.getTraceId(),
                systemLog.getLogType(),
                systemLog.getMessage(),
                systemLog.getMemberId(),
                systemLog.getStatus(),
                systemLog.getRequestUri(),
                systemLog.getCreatedAt()
        );
    }

    public SystemLogResponseDto(Long logId, String traceId, LogType logType, String message, Long memberId, LogStatus status, String requestUri, LocalDateTime createdAt) {
        this.logId = logId;
        this.traceId = traceId;
        this.logType = logType;
        this.message = message;
        this.memberId = memberId;
        this.status = status;
        this.requestUri = requestUri;
        this.createdAt = createdAt;
    }
}
