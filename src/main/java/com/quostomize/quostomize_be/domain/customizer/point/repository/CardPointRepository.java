package com.quostomize.quostomize_be.domain.customizer.point.repository;

import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPointRepository extends JpaRepository<CardPoint, Long> {
    Optional<CardPoint> findByCardDetail_CardSequenceId(Long cardSequenceId);
}
