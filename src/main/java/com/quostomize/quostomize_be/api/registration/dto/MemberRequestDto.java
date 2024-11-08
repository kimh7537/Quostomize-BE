package com.quostomize.quostomize_be.api.registration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record MemberRequestDto(
        @NotBlank(message = "이름은 필수 입력 값입니다.") String memberName,
        @NotBlank(message = "이메일은 필수 입력 값입니다.") @Email(message = "이메일 형식으로 입력해주세요.") String memberEmail,
        @NotBlank String memberLoginId,
        @NotBlank @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.") String memberPassword,
        @NotBlank @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.") String memberPasswordConfirm,
        @NotBlank String residenceNumber,
        @NotBlank String zipCode,
        @NotBlank String memberAddress,
        @NotBlank(message = "상세주소를 입력하세요.") String memberDetailAddress,
        @NotBlank String memberPhoneNumber,
        @NotBlank @Length(min = 6, max = 6, message = "2차 인증번호 6자리를 입력해주세요.") String secondaryAuthCode,
        @NotBlank @Length(min = 6, max = 6, message = "2차 인증번호 6자리를 입력해주세요.") String secondaryAuthCodeConfirm
) {}


// 관리자 추가하면 필요할 수도 있음.
//    public enum Role {
//        USER, ADMIN
//    }
//    @Enumerated(EnumType.STRING)
//    private Role role;//
