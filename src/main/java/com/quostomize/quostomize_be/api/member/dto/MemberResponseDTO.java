package com.quostomize.quostomize_be.api.member.dto;

import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.service.EncryptService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;

public record MemberResponseDTO(

        @NotNull
        @Size(max = 20)
        String memberName,

        @NotNull
        @Email
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

    public static MemberResponseDTO fromEntityWithDecodedPhoneNumber(Member member, String memberPhoneNumber) {
        return new MemberResponseDTO(
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberLoginId(),
                member.getZipCode(),
                member.getMemberAddress(),
                member.getMemberDetailAddress(),
                memberPhoneNumber
        );
    }

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
