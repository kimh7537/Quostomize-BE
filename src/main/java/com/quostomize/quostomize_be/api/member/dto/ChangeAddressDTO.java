package com.quostomize.quostomize_be.api.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public record ChangeAddressDTO (
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
