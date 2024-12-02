package com.quostomize.quostomize_be.api.auth.dto;

import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;

public record MemberRoleRequest(
        Long adminId,
        Long memberId,
        MemberRole role,
        String secondaryAuthCode
) {
}
