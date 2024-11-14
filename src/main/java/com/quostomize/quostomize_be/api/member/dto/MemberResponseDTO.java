package com.quostomize.quostomize_be.api.member.dto;

import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberResponseDTO(

        @NotNull
        @Size(max = 20)
        String memberName,

        @NotNull
        @Size(max = 50)
        String memberEmail,

        @NotNull
        @Size(max = 15)
        String memberLoginId,

        @NotNull
        @Size(max = 10)
        String zipCode,

        @NotNull
        @Size(max = 100)
        String memberAddress,

        @NotNull
        @Size(max = 100)
        String memberDetailAddress,

        @NotNull
        @Size(max = 20)
        String memberPhoneNumber

) {
    public static MemberResponseDTO fromEntity(Member member) {
        return new MemberResponseDTO(
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberLoginId(),
                member.getZipCode(),
                member.getMemberAddress(),
                member.getMemberDetailAddress(),
                member.getMemberPhoneNumber()
        );
    }

}
