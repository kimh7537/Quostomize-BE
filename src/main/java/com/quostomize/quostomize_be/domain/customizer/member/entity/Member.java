package com.quostomize.quostomize_be.domain.customizer.member.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long member_id;

    @Column(name = "member_name", length = 20, nullable = false)
    private String member_name;

    @Column(name = "member_email", length = 50, nullable = false)
    private String member_email;

    @Column(name = "member_login_id", length = 15, nullable = false)
    private String member_login_id;

    @Column(name = "member_password", length = 16, nullable = false)
    private String member_password;

    @Column(name = "residence_number", length = 13, nullable = false)
    private String residence_number;

    @Column(name = "zip_code", length = 20, nullable = false)
    private String zip_code;

    @Column(name = "member_address", length = 255, nullable = false)
    private String member_address;

    @Column(name = "member_detail_address", length = 255, nullable = true)
    private String member_detail_address;

    @Column(name = "member_phone_number", length = 20, nullable = false)
    private String member_phone_number;

    @Column(name = "secondary_auth_code", length = 6, nullable = false)
    private String secondary_auth_code;

}
