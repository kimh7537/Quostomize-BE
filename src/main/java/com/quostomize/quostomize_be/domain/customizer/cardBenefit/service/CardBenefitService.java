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

import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CardBenefitService {

    private final CustomerRepository customerRepository;
    @Value("${spring.schedule.cron}")
    private String cronExpression;

    @Value("${spring.schedule.use}")
    private boolean isSchedulerEnabled;

    LocalDateTime recentTime = LocalDateTime.now();

    private final CardBenefitRepository cardBenefitRepository;
    private final CardDetailRepository cardDetailRepository;

    public CardBenefitService(CardBenefitRepository cardBenefitRepository, CardDetailRepository cardDetailRepository, CustomerRepository customerRepository) {
        this.cardBenefitRepository = cardBenefitRepository;
        this.cardDetailRepository = cardDetailRepository;
        this.customerRepository = customerRepository;
    }

    // 혜택 내역 조회
    public List<CardBenefitResponse> findAll(long memberId) {
        long cardSequenceId = getCardSequenceIdForMember(memberId);
        Set<CardBenefit> cardBenefits = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true);
        return cardBenefits.stream().map(CardBenefitResponse::from).collect(Collectors.toList());
    }

    // 사용자의 카드id 조회
    private long getCardSequenceIdForMember(long memberId) {
        Customer customer = customerRepository.findByMember_MemberId(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CARD_NOT_FOUND));
        System.out.println("customer.getCustomerId() = " + customer.getCustomerId());
        return customer.getCustomerId();
    }

    // 혜택 변경 가능일자 계산
    public Boolean getBenefitChangeDate(CardBenefit cardBenefit) {
        return isBenefitChange(cardBenefit.getModifiedAt());
    }

    // 날짜 차이 계산 후 혜택 변경 가능여부 확인
    private Boolean isBenefitChange(LocalDateTime modifiedAt) {
        long daysDifference = ChronoUnit.DAYS.between(modifiedAt, recentTime);
        return daysDifference >= 30;
    }

    // 변경 일자에 따른 버튼 내용
    public String getBenefitChangeButtonLabel(long cardSequenceId) {
        CardBenefit cardBenefit = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true)
                .stream()
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CARD_DETAIL_BENEFIT_NOT_FOUND));
        return isBenefitChange(cardBenefit.getModifiedAt()) ? "변경하기" : "예약하기";
    }

    // 혜택 생성 메서드
    private CardBenefit createCardBenefit(CardBenefitRequest request, CardDetail cardDetail, boolean isActive) {
        return createCardBenefit(request, cardDetail, isActive, request.benefitEffectiveDate());
    }

    private CardBenefit createCardBenefit(CardBenefitRequest request, CardDetail cardDetail, boolean isActive, LocalDate localDate) {
        return CardBenefit.builder()
                .cardDetail(cardDetail)
                .benefitEffectiveDate(request.benefitEffectiveDate())
                .benefitRate(request.benefitRate())
                .isActive(isActive)
                .upperCategory(BenefitCommonCode.builder().benefitCommonId(request.upperCategoryId()).build())
                .lowerCategory(request.lowerCategoryId() != null ?
                        BenefitCommonCode.builder().benefitCommonId(request.lowerCategoryId()).build() : null)
                .build();
    }

    // 혜택 변경 유효기간 계산 메서드 분리
    private LocalDate calculateBenefitEffectiveDate(LocalDateTime modifiedAt) {
        long daysDifference = ChronoUnit.DAYS.between(modifiedAt, recentTime);
        if (daysDifference < 30) {
            return modifiedAt.toLocalDate().plusDays(30);
        }
        return null;
    }

    // 혜택 변경 적용하기
    @Transactional
    public void updateCardBenefits(List<CardBenefitRequest> cardBenefitRequests) {
        // 한번의 Request에서 cardSequenceId는 동일한 값을 가지므로 첫 번째 요청으로부터 cardSequenceId를 가져옴
        long cardSequenceId = cardBenefitRequests.get(0).cardSequenceId();
        // 1. 동일한 card_sequence_id를 가진 모든 CardBenefit의 is_active를 false로 설정 (최초 1번 호출)
        cardBenefitRepository.deactivateCardBenefitsByCardSequenceId(cardSequenceId);
        // 2. 새로운 혜택 정보 일괄 추가
        CardDetail cardDetail = cardDetailRepository.findById(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_DETAIL_NOT_FOUND));
        List<CardBenefit> cardBenefits = cardBenefitRequests.stream()
                .map(request -> createCardBenefit(request, cardDetail, true))
                .collect(Collectors.toList());
        cardBenefitRepository.saveAll(cardBenefits);
    }

    // 혜택 변경 예약 및 반영을 하나의 트랜잭션으로 처리
    @Transactional
    public void processCardBenefits(List<CardBenefitRequest> cardBenefitRequests) {
        if (!cardBenefitRequests.isEmpty()) {
            reserveCardBenefits(cardBenefitRequests);
        }
    }

    // 혜택 변경 예약하기
    private void reserveCardBenefits(List<CardBenefitRequest> cardBenefitRequests) {
        if (cardBenefitRequests.isEmpty()) {
            return;
        }

        long cardSequenceId = cardBenefitRequests.get(0).cardSequenceId();
        CardDetail cardDetail = cardDetailRepository.findById(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_DETAIL_NOT_FOUND));

        Set<CardBenefit> existingBenefit = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true);

        if (!existingBenefit.isEmpty()) {
            CardBenefit cardBenefit = existingBenefit.iterator().next();
            LocalDate benefitEffectiveDate = calculateBenefitEffectiveDate(cardBenefit.getModifiedAt());

            if (benefitEffectiveDate != null) {
                List<CardBenefit> cardBenefits = cardBenefitRequests.stream()
                        .map(request -> createCardBenefit(request, cardDetail, false, benefitEffectiveDate))
                        .collect(Collectors.toList());
                cardBenefitRepository.saveAll(cardBenefits);
            }
        }
    }

    // 예약한 혜택을 반영하는 스케줄러
    @Scheduled(cron = "${spring.schedule.cron}")
    @Transactional
    public void scheduledBenefitUpdate() {
        try {
            LocalDate today = LocalDate.now();
            Set<CardBenefit> benefitsToActivate = cardBenefitRepository.findCardBenefitsByBenefitEffectiveDateAndIsActive(today, false);
            if (!benefitsToActivate.isEmpty()) {
                updateActiveBenefits(benefitsToActivate);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.CARD_BENEFIT_RESERVE_FAILED);
        }
    }

    private void updateActiveBenefits(Set<CardBenefit> benefitsToActivate) {
        for (CardBenefit benefit : benefitsToActivate) {
            long cardSequenceid = benefit.getCardDetail().getCardSequenceId();
            cardBenefitRepository.deactivateCardBenefitsByCardSequenceId(cardSequenceid);
        }
        cardBenefitRepository.activateBenefitsForToday(LocalDate.now());
    }
}