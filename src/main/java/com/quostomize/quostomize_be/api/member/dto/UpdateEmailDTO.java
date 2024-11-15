package com.quostomize.quostomize_be.api.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateEmailDTO(
        @NotNull
        @NotBlank
        @Size(max=50)
        String newEmail
) {
}
