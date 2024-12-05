package com.quostomize.quostomize_be.api.cardapplicant.dto;

import com.quostomize.quostomize_be.domain.customizer.cardapplication.entity.CardApplicantInfo;
import jakarta.validation.constraints.*;

public record CardApplicantDetailsDTO(

        // 응답을 줄 때에는 주민등록번호 마스킹 필요
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
        public static CardApplicantDetailsDTO fromEntity(CardApplicantInfo cardApplicantInfo) {
                String maskData = maskData(cardApplicantInfo.getResidenceNumber());
                return new CardApplicantDetailsDTO(
                        maskData,
                        cardApplicantInfo.getApplicantName(),
                        cardApplicantInfo.getEnglishName(),
                        cardApplicantInfo.getZipCode(),
                        cardApplicantInfo.getShippingAddress(),
                        cardApplicantInfo.getShippingDetailAddress(),
                        cardApplicantInfo.getApplicantEmail(),
                        cardApplicantInfo.getPhoneNumber(),
                        cardApplicantInfo.getHomeAddress(),
                        cardApplicantInfo.getHomeDetailAddress()
                );
        }

        private static String maskData(String residenceNumber) {
                if (residenceNumber.length() >= 7) {
                        return residenceNumber.substring(0, residenceNumber.length() - 6) + "******";
                }
                return residenceNumber;
        }
}
