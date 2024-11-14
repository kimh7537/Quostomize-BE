package com.quostomize.quostomize_be.api.registration.dto;

import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;

public record MemberResponseDto(
        String memberLoginId,
        String name,
        String email,
        String memberPhoneNumber
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getMemberLoginId(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberPhoneNumber()
        );
    }

}

