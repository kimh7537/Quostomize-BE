package com.quostomize.quostomize_be.domain.auth.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(name = "MEMBER_EMAIL_UNIQUE", columnNames = "member_email"),
        @UniqueConstraint(name = "MEMBER_LOGIN_ID_UNIQUE", columnNames = "member_login_id"),
        @UniqueConstraint(name = "RESIDENCE_NUMBER_UNIQUE", columnNames = "residence_number"),
        @UniqueConstraint(name = "MEMBER_PHONE_NUMBER_UNIQUE", columnNames = "member_phone_number")
})
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name", length = 20, nullable = false)
    private String memberName;

    @Email
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

    @Column(name = "member_phone_number", nullable = false)
    private String memberPhoneNumber;

    @Column(name = "secondary_auth_code", nullable = false)
    private String secondaryAuthCode;

    @Column(name = "member_role", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "'MEMBER'")
    private MemberRole role;

    @Builder
    public Member(Long memberId, String memberName, String memberEmail, String memberLoginId, String memberPassword, String residenceNumber, String zipCode, String memberAddress, String memberDetailAddress, String memberPhoneNumber, String secondaryAuthCode, MemberRole role) {
        this.memberId = memberId;
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
        this.role = role != null ? role : MemberRole.MEMBER; // 기본값 설정
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }

    public void updatePassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public void updateSecondaryAuthCode(String secondaryAuthCode) {
        this.secondaryAuthCode = secondaryAuthCode;
    }

    public void updatePhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }


}