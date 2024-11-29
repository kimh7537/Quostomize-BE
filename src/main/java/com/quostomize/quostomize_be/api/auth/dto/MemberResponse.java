package com.quostomize.quostomize_be.api.auth.dto;

import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;

import java.time.LocalDateTime;

public record MemberResponse(
        Long memberId,
        String memberName,
        String memberEmail,
        String memberLoginId,
        String zipCode,
        String memberAddress,
        String memberDetailAddress,
        MemberRole role,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
