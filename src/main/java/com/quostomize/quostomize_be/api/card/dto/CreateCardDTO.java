package com.quostomize.quostomize_be.api.card.dto;

import com.quostomize.quostomize_be.api.cardapplicant.CardApplicantDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record CreateCardDTO(
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
        int paymentReceiptMethods
) {

    @Builder
    public static CreateCardDTO fromApplicant(CardApplicantDTO cardApplicantDTO) {
        return new CreateCardDTO(
                cardApplicantDTO.cardColor(),
                cardApplicantDTO.cardBrand(),
                cardApplicantDTO.isAppCard(),
                cardApplicantDTO.isForeignBlocked(),
                cardApplicantDTO.isPostpaidTransport(),
                cardApplicantDTO.cardPassword(),
                cardApplicantDTO.cardPasswordConfirm(),
                cardApplicantDTO.optionalTerms(),
                cardApplicantDTO.paymentReceiptMethods()
        );
    }
}
