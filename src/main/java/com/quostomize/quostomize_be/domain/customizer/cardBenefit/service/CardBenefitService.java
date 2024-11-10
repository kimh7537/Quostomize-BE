package com.quostomize.quostomize_be.domain.customizer.cardBenefit.service;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitRequest;
import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.BenefitCommonCode;
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
        Long cardId = 2L;
        boolean isActive = true;
        Set<CardBenefit> cardBenefits = cardBenefitRepository.findCardBenefitsByCardCardIdAndIsActive(cardId, isActive);
        return cardBenefits.stream().map(CardBenefitResponse::from).collect(Collectors.toList());
    }

    public void updateCardBenefits(List<CardBenefitRequest> cardBenefitRequests) {
        // cardId와 isActive로 CardBenefit 목록 검색
        Set<CardBenefit> cardBenefits;
        for (CardBenefitRequest request : cardBenefitRequests) {
            cardBenefits = cardBenefitRepository.findCardBenefitsByCardCardIdAndIsActive(request.cardId(), request.isActive());

            // 해당 benefitId에 맞는 CardBenefit을 찾고 업데이트
            cardBenefits.stream()
                    .filter(cb -> cb.getBenefitId().equals(request.benefitId()))
                    .findFirst()
                    .ifPresent(cardBenefit -> {
                        cardBenefitRepository.save(
                                cardBenefit.builder()
                                        .benefitRate(request.benefitRate())
                                        .isActive(request.isActive())
                                        .upperCategory(BenefitCommonCode.builder().benefitCommonId(request.upperCategoryId()).build())
                                        .lowerCategory(BenefitCommonCode.builder().benefitCommonId(request.lowerCategoryId()).build())
                                        .build()
                        );
                    });
        }
    }
}