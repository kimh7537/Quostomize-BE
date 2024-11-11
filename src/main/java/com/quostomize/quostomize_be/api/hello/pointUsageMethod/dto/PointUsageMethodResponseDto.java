package com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto;


public record PointUsageMethodResponseDto (
        Long pointUsageTypeId,
        Boolean isLotto,
        Boolean isPayback,
        Boolean isPieceStock

) {}
