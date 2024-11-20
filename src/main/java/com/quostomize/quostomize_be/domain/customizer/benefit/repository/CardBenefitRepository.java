package com.quostomize.quostomize_be.domain.customizer.benefit.repository;

import com.quostomize.quostomize_be.domain.customizer.benefit.entity.CardBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefit, Long> {

List<CardBenefit> findByCardDetail_CardSequenceId(Long cardId);

}
