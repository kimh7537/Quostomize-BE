package com.quostomize.quostomize_be.api.email.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AdminEmailRequest(
        @NotNull String title,
        @NotNull int optionalTerms,
        @NotNull MultipartFile htmlFile
) {
}
