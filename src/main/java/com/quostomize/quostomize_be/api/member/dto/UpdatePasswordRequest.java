package com.quostomize.quostomize_be.api.member.dto;

import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequest(
        @NotNull
        String password
) {
}
