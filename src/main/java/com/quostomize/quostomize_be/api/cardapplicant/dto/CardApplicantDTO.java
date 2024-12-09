package com.quostomize.quostomize_be.api.cardapplicant.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record CardApplicantDTO(

        // 카드 기본 정보
        @NotNull
        int cardColor,
        @NotNull
        int cardBrand,
        @NotNull
        boolean isAppCard,
        @NotNull
        boolean isForeignBlocked,
        @NotNull
        boolean isPostpaidTransport,
        @NotNull
        String cardPassword,
        @NotNull
        String cardPasswordConfirm,
        @NotNull
        int optionalTerms,
        @NotNull
        int paymentReceiptMethods,

        // 카드 혜택 정보
        @NotNull
        List<CardBenefitInfo> benefits,

        // 포인트 사용 방법
        @NotNull
        Boolean isLotto,
        @NotNull
        Boolean isPayback,
        @NotNull
        Boolean isPieceStock,

        // 신청자 정보
        @NotNull
        String residenceNumber,
        @NotNull
        @Size(max = 40)
        String applicantName,
        @NotNull
        @Size(max = 40)
        @Pattern(regexp = "^[a-zA-Z]+$")
        String englishName,
        @NotNull
        @Size(max = 10)
        String zipCode,
        @NotNull
        @Size(max = 100)
        String shippingAddress,
        @NotNull
        @Size(max = 100)
        String shippingDetailAddress,
        @NotNull
        @NotBlank
        @Email
        @Size(max = 50)
        String applicantEmail,
        @NotNull
        @Size(max = 20)
        String phoneNumber,
        @NotNull
        @Size(max = 100)
        String homeAddress,
        @NotNull
        @Size(max = 100)
        String homeDetailAddress
) {
    // 혜택 정보를 담는 내부 record
    public record CardBenefitInfo(
            @NotNull
            Long upperCategoryId,
            Long lowerCategoryId,
            @NotNull
            @Min(0)
            @Max(4)
            Integer benefitRate
    ) {}
}
