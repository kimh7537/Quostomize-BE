package com.quostomize.quostomize_be.api.cardapplicant.dto;

import jakarta.validation.constraints.*;

public record CardApplicantDTO(

        /*
         * 카드정보에 들어갈 정보들
         * */

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

        /*
         * 카드 신청인 정보에 들어갈 정보
         * */

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
        String homeDetailAddress,

        @NotNull
        Boolean isLotto,

        @NotNull
        Boolean isPayback,

        @NotNull
        Boolean isPieceStock
) {
}
