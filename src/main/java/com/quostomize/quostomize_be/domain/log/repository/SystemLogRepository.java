package com.quostomize.quostomize_be.domain.log.repository;

import com.quostomize.quostomize_be.domain.log.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.quostomize.quostomize_be.domain.log.enums.LogType;

import java.util.List;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    Page<SystemLog> findByLogTypeIn(List<LogType> logTypes, Pageable pageable);
}
