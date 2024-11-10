package com.quostomize.quostomize_be.domain.customizer.cardBenefit.service;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository.CardBenefitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardBenefitService {
    LocalDateTime recentTime = LocalDateTime.now();

    private final CardBenefitRepository cardBenefitRepository;

    public CardBenefitService(CardBenefitRepository cardBenefitRepository) {
        this.cardBenefitRepository = cardBenefitRepository;
    }
    
    // 혜택 내역 조회
    public List<CardBenefitResponse> findAll() {
        // TODO: 하드코딩된 customer 정보 변경 필요
        Long cardId = 2L;
        boolean isActive = true;
        Set<CardBenefit> cardBenefits = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardId, isActive);
        return cardBenefits.stream().map(CardBenefitResponse::from).collect(Collectors.toList());
    }

    // 혜택 변경 가능일자 계산
    public Boolean getBenefitChangeDate(CardBenefit cardBenefit) {
        LocalDateTime createdAt = cardBenefit.getCreatedAt();
        long daysDifference = ChronoUnit.DAYS.between(createdAt, recentTime);
        return daysDifference >= 30;
    }
    
    // 변경 일자에 따른 버튼 내용
    public String getBenefitChangeButtonLabel(CardBenefit cardBenefit) {
        LocalDateTime createdAt = cardBenefit.getCreatedAt();
        long daysDifference = ChronoUnit.DAYS.between(createdAt, recentTime);
        return daysDifference >= 30 ? "변경하기" : "예약하기";
    }

}