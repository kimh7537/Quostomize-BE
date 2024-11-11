package com.quostomize.quostomize_be.domain.customizer.point.repository;

import com.quostomize.quostomize_be.domain.customizer.point.entity.PointUsageMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointUsageMethodRepository extends JpaRepository<PointUsageMethod, Long> {
    List<PointUsageMethod> findAllByIsLottoTrue();

    PointUsageMethod findByCardDetail_CardSequenceId(Long cardSequenceId);
}
