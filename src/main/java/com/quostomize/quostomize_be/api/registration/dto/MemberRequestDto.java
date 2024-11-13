package com.quostomize.quostomize_be.api.registration.dto;

import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;

public record MemberRequestDto(
        String memberName,
        String memberEmail,
        String memberLoginId,
        String memberPassword,
        String memberPasswordConfirm,
        String residenceNumber,
        String zipCode,
        String memberAddress,
        String memberDetailAddress,
        String memberPhoneNumber,
        String secondaryAuthCode,
        String secondaryAuthCodeConfirm
) {
    public static MemberRequestDto from(Member member) {
        return new MemberRequestDto(
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberLoginId(),
                member.getMemberPassword(),
                null,
                member.getResidenceNumber(),
                member.getZipCode(),
                member.getMemberAddress(),
                member.getMemberDetailAddress(),
                member.getMemberPhoneNumber(),
                member.getSecondaryAuthCode(),
                null
        );
    }

    public Member toEntity() {
        return Member.builder()
                .memberName(this.memberName())
                .memberEmail(this.memberEmail())
                .memberLoginId(this.memberLoginId())
                .memberPassword(this.memberPassword())
                .residenceNumber(this.residenceNumber())
                .zipCode(this.zipCode())
                .memberAddress(this.memberAddress())
                .memberDetailAddress(this.memberDetailAddress())
                .memberPhoneNumber(this.memberPhoneNumber())
                .secondaryAuthCode(this.secondaryAuthCode())
                .build();
    }

    public boolean isValidPassword() {
        return this.memberPassword().equals(this.memberPasswordConfirm());
    }

    public boolean isValidSecondaryAuthCode() {
        return this.secondaryAuthCode().equals(this.secondaryAuthCodeConfirm());
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\d{10,11}$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
