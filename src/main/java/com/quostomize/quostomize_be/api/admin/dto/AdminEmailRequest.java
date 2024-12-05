package com.quostomize.quostomize_be.api.admin.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AdminEmailRequest(
        @NotNull String title,
        Integer optionalTerms,
        @NotNull MultipartFile htmlFile
) {
}
