package com.quostomize.quostomize_be.api.registration.dto;

import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record MemberRequestDto(
        @NotBlank(message = "이름은 필수 입력 값입니다.") String memberName,
        @NotBlank(message = "이메일은 필수 입력 값입니다.") @Email(message = "이메일 형식으로 입력해주세요.") String memberEmail,
        @NotBlank String memberLoginId,
        String role,
        @NotBlank @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.") String memberPassword,
        @NotBlank @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.") String memberPasswordConfirm,
        @NotBlank @Length(min=13, max=13) String residenceNumber,
        @NotBlank String zipCode,
        @NotBlank String memberAddress,
        @NotBlank(message = "상세주소를 입력하세요.") String memberDetailAddress,
        @NotBlank String memberPhoneNumber,
        @NotBlank @Length(min = 6, max = 6, message = "2차 인증번호 6자리를 입력해주세요.") String secondaryAuthCode,
        @NotBlank @Length(min = 6, max = 6, message = "2차 인증번호 6자리를 입력해주세요.") String secondaryAuthCodeConfirm
) {
    public static MemberRequestDto from(Member member) {
        return new MemberRequestDto(
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberLoginId(),
                member.getRole(),
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
                .role(this.role == null || this.role.isEmpty() ? "USER" : this.role())
                .memberPassword(this.memberPassword())
                .residenceNumber(this.residenceNumber())
                .zipCode(this.zipCode())
                .memberAddress(this.memberAddress())
                .memberDetailAddress(this.memberDetailAddress())
                .memberPhoneNumber(this.memberPhoneNumber())
                .secondaryAuthCode(this.secondaryAuthCode())
                .build();
    }


    public boolean isValidMemberLoginId(String memberLoginId) {
        return memberLoginId != null & memberLoginId.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
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

}