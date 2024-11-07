package com.quostomize.quostomize_be.domain.customizer.cardBenefit.service;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository.CardBenefitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardBenefitService {
    private final CardBenefitRepository cardBenefitRepository;

    public CardBenefitService(CardBenefitRepository cardBenefitRepository) {
        this.cardBenefitRepository = cardBenefitRepository;
    }

    public List<CardBenefitResponse> findAll() {
        // TODO: 하드코딩된 customer 정보 변경 필요
        Long cardId = 1L;
        boolean isActive = true;
        Set<CardBenefit> cardBenefits = cardBenefitRepository.findCardBenefitsByCardCardIdAndIsActive(cardId, isActive);
        return cardBenefits.stream().map(CardBenefitResponse::from).collect(Collectors.toList());
    }
}