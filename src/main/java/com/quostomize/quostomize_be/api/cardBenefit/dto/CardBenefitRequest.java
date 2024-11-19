package com.quostomize.quostomize_be.api.cardBenefit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record CardBenefitRequest(
        LocalDate benefitEffectiveDate,
        Integer benefitRate,
        @NotNull Boolean isActive,
        @NotNull Long cardSequenceId,
        @NotNull Long upperCategoryId,
        Long lowerCategoryId,
        @NotBlank(message = "2차 비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^[0-9]{6}$", message = "2차 비밀번호는 6자리 숫자여야 합니다.")
        String secondaryAuthCode
) {
    // benefitEffectiveDate가 null이면 now를 반환하도록
    public LocalDate benefitEffectiveDate() {
        return benefitEffectiveDate != null ? benefitEffectiveDate : LocalDate.now();
    }
    
    // 카드혜택은 여러 줄이 동시에 변경될 수 있음
    // CardBenefitUpdateRequest: 여러 개의 CardBenefitRequest 객체를 담아서 전달하기 위함
    public static record CardBenefitUpdateRequest(List<CardBenefitRequest> cardBenefits) {}
}
