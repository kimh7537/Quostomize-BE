package com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record PointUsageMethodResponseDto (
        Long pointUsageTypeId,
        Boolean isLotto,
        Boolean isPayback,
        Boolean isPieceStock

) {}
