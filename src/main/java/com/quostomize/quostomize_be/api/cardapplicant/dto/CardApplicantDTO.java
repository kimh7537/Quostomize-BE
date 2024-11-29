package com.quostomize.quostomize_be.api.cardapplicant.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record CardApplicantDTO(

        // 카드 기본 정보
        @NotNull
        @NotBlank
        int cardColor,
        @NotNull
        @NotBlank
        int cardBrand,
        @NotNull
        @NotBlank
        boolean isAppCard,
        @NotNull
        @NotBlank
        boolean isForeignBlocked,
        @NotNull
        @NotBlank
        boolean isPostpaidTransport,
        @NotNull
        @NotBlank
        String cardPassword,
        @NotNull
        @NotBlank
        String cardPasswordConfirm,
        @NotNull
        @NotBlank
        int optionalTerms,
        @NotNull
        @NotBlank
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
        @NotBlank
        String residenceNumber,
        @NotNull
        @NotBlank
        @Size(max = 40)
        String applicantName,
        @NotNull
        @NotBlank
        @Size(max = 40)
        @Pattern(regexp = "^[a-zA-Z]+$")
        String englishName,
        @NotNull
        @NotBlank
        @Size(max = 10)
        String zipCode,
        @NotNull
        @NotBlank
        @Size(max = 100)
        String shippingAddress,
        @NotNull
        @NotBlank
        @Size(max = 100)
        String shippingDetailAddress,
        @NotNull
        @NotBlank
        @Email
        @Size(max = 50)
        String applicantEmail,
        @NotNull
        @NotBlank
        @Size(max = 20)
        String phoneNumber,
        @NotNull
        @NotBlank
        @Size(max = 100)
        String homeAddress,
        @NotNull
        @NotBlank
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
