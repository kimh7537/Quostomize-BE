package com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository;

import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {
    @Query("select cb from CardBenefit cb where cb.cardDetail.cardSequenceId = :cardSequenceId and cb.isActive = :isActive")
    Set<CardBenefit> findCardBenefitsByCardDetailCardSequenceIdAndIsActive(@Param("cardSequenceId") long cardSequenceId, @Param("isActive") boolean isActive);
    
    @Modifying
    @Query("update CardBenefit cb set cb.isActive = false where cb.cardDetail.cardSequenceId = :cardSequenceId")
    void deactivateCardBenefitsByCardSequenceId(@Param("cardSequenceId") long cardSequenceId);

    Set<CardBenefit> findCardBenefitsByBenefitEffectiveDateAndIsActive(LocalDate effectiveDate, boolean isActive);

    @Modifying
    @Query("update CardBenefit cb set cb.isActive = true where cb.benefitEffectiveDate = :today and cb.isActive = false")
    void activateBenefitsForToday(@Param("today") LocalDate today);

    List<CardBenefit> findByCardDetail_CardSequenceId(Long cardId);
}
