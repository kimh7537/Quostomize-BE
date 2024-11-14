package com.quostomize.quostomize_be.api.cardBenefit.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

public record CardBenefitRequest(
        @NotNull
        LocalDate benefitEffectiveDate,
        @NotNull @Range(min = 0, max = 4, message = "혜택률은 최소 0%, 최대 4%로 설정할 수 있습니다.")
        Integer benefitRate,
        @NotNull
        Boolean isActive,
        @NotNull
        Long cardSequenceId,
        @NotNull
        Long upperCategoryId,
        Long lowerCategoryId
) {
    // benefitEffectiveDate가 null이면 now를 반환하도록
    public LocalDate benefitEffectiveDate() {
        return benefitEffectiveDate != null ? benefitEffectiveDate : LocalDate.now();
    }
    
    // 카드혜택은 여러 줄이 동시에 변경될 수 있음
    // CardBenefitUpdateRequest: 여러 개의 CardBenefitRequest 객체를 담아서 전달하기 위함
    public static record CardBenefitUpdateRequest(List<CardBenefitRequest> cardBenefits) {}
}
