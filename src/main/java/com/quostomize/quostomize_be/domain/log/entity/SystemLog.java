package com.quostomize.quostomize_be.domain.log.entity;

import com.quostomize.quostomize_be.domain.log.enums.LogStatus;
import com.quostomize.quostomize_be.domain.log.enums.LogType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "system_logs")
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(nullable = false)
    private String traceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LogType logType;

    @Column(nullable = false)
    private String message;

    @Column
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LogStatus status;

    @Column(nullable = true)
    private String requestUri;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
