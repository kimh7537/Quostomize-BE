package com.quostomize.quostomize_be.api.auth.dto;

import com.quostomize.quostomize_be.domain.auth.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record JoinResponse(
        @Schema(description = "가입된 회원 PK")
        Long memberId
) {
    public static JoinResponse from(Member member) {
        return new JoinResponse(
                member.getMemberId()
        );
    }
}