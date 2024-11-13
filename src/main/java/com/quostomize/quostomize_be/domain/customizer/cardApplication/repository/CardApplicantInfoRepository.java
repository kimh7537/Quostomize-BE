package com.quostomize.quostomize_be.domain.customizer.cardApplication.repository;

import com.quostomize.quostomize_be.domain.customizer.cardApplication.entity.CardApplicantInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardApplicantInfoRepository extends JpaRepository<CardApplicantInfo, Long> {
    Optional<CardApplicantInfo> findByCardApplicantInfoId(Long cardApplicationInfoId);
}
