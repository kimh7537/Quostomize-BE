package com.quostomize.quostomize_be.api.admin.controller;

import com.quostomize.quostomize_be.api.admin.dto.SystemLogResponseDto;
import com.quostomize.quostomize_be.domain.log.entity.SystemLog;
import com.quostomize.quostomize_be.domain.log.enums.LogType;
import com.quostomize.quostomize_be.domain.log.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/admin/logs")
@RequiredArgsConstructor
public class LogController {

    private final SystemLogRepository systemLogRepository;

    @GetMapping("/login-logout")
    public ResponseEntity<Page<SystemLogResponseDto>> getLoginLogoutLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SystemLog> logPage = systemLogRepository.findByLogTypeIn(
                List.of(LogType.LOGIN_ATTEMPT, LogType.LOGIN_SUCCESS, LogType.LOGIN_FAILURE, LogType.LOGOUT), pageable
        );

        Page<SystemLogResponseDto> responseDtoPage = logPage.map(SystemLogResponseDto::fromEntity);

        return ResponseEntity.ok(responseDtoPage);
    }
}
