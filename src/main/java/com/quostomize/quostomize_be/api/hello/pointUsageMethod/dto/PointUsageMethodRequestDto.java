package com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record PointUsageMethodRequestDto (
        String usageType,
        boolean isActive
) {}
