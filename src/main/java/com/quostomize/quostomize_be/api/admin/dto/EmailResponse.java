package com.quostomize.quostomize_be.api.admin.dto;

import jakarta.validation.constraints.Email;

public record EmailResponse(
        @Email String email,
        String authNum
) {
}
