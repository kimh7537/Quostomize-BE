package com.quostomize.quostomize_be.api.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateEmailDTO(
        @NotNull
        @NotBlank
        @Email
        @Size(max=50)
        String newEmail
) {
}
