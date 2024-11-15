package com.quostomize.quostomize_be.api.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePhoneNumberDTO(
        @NotNull
        @NotBlank
        @Size(max = 100)
        String newPhoneNumber
        ) {
}
