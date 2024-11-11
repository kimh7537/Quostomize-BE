package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository;

import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointUsageMethodRepository extends JpaRepository <PointUsageMethod, Long> {
    Optional<PointUsageMethod> findByCardSequenceId(Long cardSequenceId);
}
