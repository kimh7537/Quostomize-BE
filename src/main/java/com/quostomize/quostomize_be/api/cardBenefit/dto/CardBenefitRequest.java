package com.quostomize.quostomize_be.api.cardBenefit.dto;


import java.util.List;

// TODO: validation 적용하기
public record CardBenefitRequest(
        Integer benefitRate,
        Boolean isActive,
        Long cardSequenceId,
        Long upperCategoryId,
        Long lowerCategoryId
) {
    // 카드혜택은 여러 줄이 동시에 변경될 수 있음
    // CardBenefitUpdateRequest: 여러 개의 CardBenefitRequest 객체를 담아서 전달하기 위함
    public static record CardBenefitUpdateRequest(List<CardBenefitRequest> cardBenefits) {}
}
