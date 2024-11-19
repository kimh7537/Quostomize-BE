package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository;

import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointUsageMethodRepository extends JpaRepository <PointUsageMethod, Long> {
    Optional<PointUsageMethod> findByCardDetail_CardSequenceId(Long cardSequenceId);

    List<PointUsageMethod> findAllByIsLottoTrue();
}
