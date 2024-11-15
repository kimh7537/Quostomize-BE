package com.quostomize.quostomize_be.domain.customizer.cardBenefit.service;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitRequest;
import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.BenefitCommonCode;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository.CardBenefitRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CardBenefitService {

    @Value("${spring.schedule.cron}")
    private String cronExpression;

    @Value("${spring.schedule.use}")
    private boolean isSchedulerEnabled;

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
        Long cardSequenceId = 2L;
        boolean isActive = true;
        Set<CardBenefit> cardBenefits = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, isActive);
        return cardBenefits.stream().map(CardBenefitResponse::from).collect(Collectors.toList());
    }

    // 혜택 변경 가능일자 계산
    public Boolean getBenefitChangeDate(CardBenefit cardBenefit) {
        LocalDateTime modifiedAt = cardBenefit.getModifiedAt();
        long daysDifference = ChronoUnit.DAYS.between(modifiedAt, recentTime);
        return daysDifference >= 30;
    }

    // 변경 일자에 따른 버튼 내용
    public String getBenefitChangeButtonLabel(Long cardSequenceId) {
        CardBenefit cardBenefit = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true)
                .stream()
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CARD_DETAIL_BENEFIT_NOT_FOUND));
        LocalDateTime modifiedAt = cardBenefit.getModifiedAt();
        long daysDifference = ChronoUnit.DAYS.between(modifiedAt, recentTime);
        return daysDifference >= 30 ? "변경하기" : "예약하기";
    }

    // 혜택 변경 적용하기
    @Transactional
    public void updateCardBenefits(List<CardBenefitRequest> cardBenefitRequests) {
        // 한번의 Request에서 cardSequenceId는 동일한 값을 가지므로 첫 번째 요청으로부터 cardSequenceId를 가져옴
        Long cardSequenceId = cardBenefitRequests.get(0).cardSequenceId();

            // 1. 동일한 card_sequence_id를 가진 모든 CardBenefit의 is_active를 false로 설정 (최초 1번 호출)
            cardBenefitRepository.deactivateCardBenefitsByCardSequenceId(cardSequenceId);

            // 2. 새로운 혜택 정보 일괄 추가
                CardDetail cardDetail = cardDetailRepository.findById(cardSequenceId)
                        .orElseThrow(() -> new AppException(ErrorCode.CARD_DETAIL_NOT_FOUND));

                List<CardBenefit> cardBenefits = cardBenefitRequests.stream()
                        .map(request -> CardBenefit.builder()
                                        .cardDetail(cardDetail)
                                        .benefitEffectiveDate(request.benefitEffectiveDate())
                                        .benefitRate(request.benefitRate())
                                        .isActive(true)
                                        .upperCategory(BenefitCommonCode.builder().benefitCommonId(request.upperCategoryId()).build())
                                        .lowerCategory(request.lowerCategoryId() != null ?
                                                BenefitCommonCode.builder().benefitCommonId(request.lowerCategoryId()).build() : null)
                                        .build())
                        .collect(Collectors.toList());
                // saveAll을 사용하여 모든 혜택 한 번에 저장
                cardBenefitRepository.saveAll(cardBenefits);
    }
    
//    // 혜택 변경 예약하기
//    public void reserveCardBenefits(List<CardBenefitRequest> cardBenefitRequests) {
//        for (CardBenefitRequest request : cardBenefitRequests) {
//            Long cardSequenceId = request.cardSequenceId();
//
//            // 1. benefitEffectiveDate 구하기
//            Set<CardBenefit> existingBenefit = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true);
//            if (existingBenefit != null && !existingBenefit.isEmpty()) {
//                CardBenefit existingCardBenefit = existingBenefit.iterator().next();
//                LocalDateTime modifiedAt = existingCardBenefit.getCreatedAt();
//                long daysDifference = ChronoUnit.DAYS.between(modifiedAt, recentTime);
//
//                if (daysDifference < 30) {
//                    LocalDate benefitEffectiveDate = modifiedAt.toLocalDate().plusDays(30);
//
//                    // 2. 예약 혜택을 새로운 혜택으로 저장하기
//                    CardDetail cardDetail = cardDetailRepository.findById(request.cardSequenceId())
//                            .orElseThrow(() -> new AppException(ErrorCode.CARD_DETAIL_NOT_FOUND));
//                    cardBenefitRepository.save(
//                            CardBenefit.builder()
//                                    .cardDetail(cardDetail)
//                                    .benefitEffectiveDate(benefitEffectiveDate)
//                                    .benefitRate(request.benefitRate())
//                                    .isActive(false)
//                                    .upperCategory(BenefitCommonCode.builder().benefitCommonId(request.upperCategoryId()).build())
//                                    .lowerCategory(request.lowerCategoryId() != null ?
//                                            BenefitCommonCode.builder().benefitCommonId(request.lowerCategoryId()).build() : null)
//                                    .build()
//                    );
//                }
//            }
//        }
//    }
//
//    // 예약한 혜택을 반영하는 스케줄러
//    @Scheduled(cron = "${spring.schedule.cron}")
//    public void updateActiveBenefits() {
//        LocalDate today = LocalDate.now();
//
//        // 1. 활성화될 예약 혜택 조회
//        Set<CardBenefit> benefitsToActivate = cardBenefitRepository.findCardBenefitsByBenefitEffectiveDateAndIsActive(today, false);
//
//        for (CardBenefit benefit : benefitsToActivate) {
//            Long cardSequenceid = benefit.getCardDetail().getCardSequenceId();
//            // 2. 기존 활성화된 혜택 비활성화
//            cardBenefitRepository.deactivateCardBenefitsByCardSequenceId(cardSequenceid, recentTime);
//            // 3. 예약된 혜택 활성화 상태로 업데이트
//            cardBenefitRepository.activateCardBenefitById(benefit.getBenefitId());
//        }
//    }
}