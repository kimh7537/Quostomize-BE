package com.quostomize.quostomize_be.api.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
        @NotNull
        String accessToken
) {
}
