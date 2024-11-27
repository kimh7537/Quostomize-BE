package com.quostomize.quostomize_be.api.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAddressDTO(
        @NotNull
        @NotBlank
        @Size(max = 10)
        String zipCode,

        @NotNull
        @NotBlank
        @Size(max = 100)
        String newAddress,

        @NotNull
        @NotBlank
        @Size(max = 100)
        String newDetailAddress
) {
}
