package com.quostomize.quostomize_be.api.auth.dto;

public record LoginResponse(
        String message,
        String memberRole,
        String cardStatus
) {
}
