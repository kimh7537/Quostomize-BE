package com.quostomize.quostomize_be.domain.customizer.registration.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "members",
        uniqueConstraints = {@UniqueConstraint(name = "MEMBER_EMAIL_MEMBER_LOGIN_ID_RESIDENCE_NUMBER_MEMBER_PHONE_NUMBER_UNIQUE",
                columnNames = {"MEMBER_EMAIL", "MEMBER_LOGIN_ID", "RESIDENCE_NUMBER", "MEMBER_PHONE_NUMBER"})})
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name", length = 20, nullable = false)
    private String memberName;

    @Column(name = "member_email", length = 50, nullable = false)
    private String memberEmail;

    @Column(name = "member_login_id", length = 15, nullable = false)
    private String memberLoginId;

    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    @Column(name = "residence_number", nullable = false)
    private String residenceNumber;

    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;

    @Column(name = "member_address", length = 100, nullable = false)
    private String memberAddress;

    @Column(name = "member_detail_address", length = 100, nullable = false)
    private String memberDetailAddress;

    @Column(name = "member_phone_number", length = 20, nullable = false)
    private String memberPhoneNumber;

    @Column(name = "secondary_auth_code", nullable = false)
    private String secondaryAuthCode;

    @Builder
    public Member(String memberName, String memberEmail, String memberLoginId, String memberPassword,
                  String residenceNumber, String zipCode, String memberAddress, String memberDetailAddress,
                  String memberPhoneNumber, String secondaryAuthCode) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberLoginId = memberLoginId;
        this.memberPassword = memberPassword;
        this.residenceNumber = residenceNumber;
        this.zipCode = zipCode;
        this.memberAddress = memberAddress;
        this.memberDetailAddress = memberDetailAddress;
        this.memberPhoneNumber = memberPhoneNumber;
        this.secondaryAuthCode = secondaryAuthCode;
    }
}