package com.quostomize.quostomize_be.api.admin.dto;

import jakarta.validation.constraints.Email;

public record EmailRequest(
        @Email String email
) {
}
