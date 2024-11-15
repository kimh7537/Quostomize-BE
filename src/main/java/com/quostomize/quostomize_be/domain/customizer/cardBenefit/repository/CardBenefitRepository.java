package com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository;

import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {
    Set<CardBenefit> findCardBenefitsByCardDetailCardSequenceIdAndIsActive(Long cardSequenceId, boolean isActive);
    
    // TODO: update문이 3번 반복되는 문제 해결 필요
    @Modifying
    @Query("update CardBenefit cb set cb.isActive = false where cb.cardDetail.cardSequenceId = :cardSequenceId")
    void deactivateCardBenefitsByCardSequenceId(@Param("cardSequenceId") Long cardSequenceId);

    Set<CardBenefit> findCardBenefitsByBenefitEffectiveDateAndIsActive(LocalDate effectiveDate, boolean isActive);

    @Modifying
    @Query("UPDATE CardBenefit cb SET cb.isActive = true WHERE cb.benefitId = :benefitId")
    void activateCardBenefitById(@Param("benefitId") Long benefitId);
}
