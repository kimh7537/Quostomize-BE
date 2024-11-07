package com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository;

import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {
    Set<CardBenefit> findCardBenefitsByCardCardIdAndIsActive(Long cardId, boolean isActive);
}
