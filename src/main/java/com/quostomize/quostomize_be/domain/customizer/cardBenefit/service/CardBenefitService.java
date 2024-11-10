package com.quostomize.quostomize_be.domain.customizer.cardBenefit.service;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitRequest;
import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.BenefitCommonCode;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
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
    private final CardDetailRepository cardDetailRepository;

    public CardBenefitService(CardBenefitRepository cardBenefitRepository, CardDetailRepository cardDetailRepository) {
        this.cardBenefitRepository = cardBenefitRepository;
        this.cardDetailRepository = cardDetailRepository;
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

    public void updateCardBenefits(List<CardBenefitRequest> cardBenefitRequests) {
        // cardId와 isActive로 CardBenefit 목록 검색
        for (CardBenefitRequest request : cardBenefitRequests) {
            Long cardSequenceId = request.cardSequenceId();

            // 1. 동일한 card_ssequence_id를 가진 모든 CardBenefit의 is_active를 false로 설정
            CardBenefit existingBenefit = cardBenefitRepository.deactivateCardBenefitsByCardSequenceId(cardSequenceId, recentTime);

            // 2. 새로운 CardBenefit으로 업데이트
            if (existingBenefit != null && getBenefitChangeDate(existingBenefit)) {
                CardDetail cardDetail = cardDetailRepository.findById(request.cardSequenceId())
                        .orElseThrow(() -> new RuntimeException("Card Detail을 찾을 수 없습니다."));
                cardBenefitRepository.save(
                        CardBenefit.builder()
                                .cardDetail(cardDetail)
                                .benefitRate(request.benefitRate())
                                .isActive(true)
                                .upperCategory(BenefitCommonCode.builder().benefitCommonId(request.upperCategoryId()).build())
                                .lowerCategory(request.lowerCategoryId() != null ?
                                        BenefitCommonCode.builder().benefitCommonId(request.lowerCategoryId()).build() : null)
                                .build()
                );
            }
        }
    }
}