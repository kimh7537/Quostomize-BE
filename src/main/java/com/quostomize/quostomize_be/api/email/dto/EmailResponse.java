package com.quostomize.quostomize_be.api.email.dto;

import jakarta.validation.constraints.Email;

public record EmailResponse(
        @Email String email,
        String authNum
) {
}
