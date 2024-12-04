package com.quostomize.quostomize_be.domain.log.repository;

import com.quostomize.quostomize_be.domain.log.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
}
