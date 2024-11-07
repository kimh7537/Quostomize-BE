package com.quostomize.quostomize_be.api.registration.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank
    private String memberLoginId;

    @NotBlank
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String memberPassword;

    @NotBlank
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String memberPasswordConfirm;

    @NotBlank
    private String residenceNumber;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String memberAddress;

    @NotBlank(message = "상세주소를 입력하세요.")
    private String memberDetailAddress;

    @NotBlank
    private String memberPhoneNumber;

    @NotBlank
    private String secondaryAuthCode;

    @NotBlank
    private String secondaryAuthCodeConfirm;

// 관리자 추가하면 필요할 수도 있음.
//    public enum Role {
//        USER, ADMIN
//    }
//
//    @Enumerated(EnumType.STRING)
//    private Role role;//

}