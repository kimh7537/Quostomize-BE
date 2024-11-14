package com.quostomize.quostomize_be.domain.customizer.cardapplication.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card_applicant_info")
public class CardApplicantInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_applicant_info_id")
    private Long cardApplicantInfoId;

    @Column(name = "residence_number", nullable = false)
    private String residenceNumber;

    @Column(name = "applicant_name", length = 40, nullable = false)
    private String applicantName;

    @Column(name = "english_name", length = 40, nullable = false)
    private String englishName;

    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;

    @Column(name = "shipping_address", length = 100, nullable = false)
    private String shippingAddress;

    @Column(name = "shipping_detail_address", length = 100, nullable = false)
    private String shippingDetailAddress;

    @Column(name = "applicant_email", length = 50, nullable = false)
    private String applicantEmail;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "home_address", length = 100, nullable = false)
    private String homeAddress;

    @Column(name = "home_detail_address", length = 100, nullable = false)
    private String homeDetailAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_sequence_id", nullable = false)
    private CardDetail cardDetail;

    @Builder
    public CardApplicantInfo(CardDetail cardDetail, String homeDetailAddress, String homeAddress, String phoneNumber, String applicantEmail, String shippingDetailAddress, String shippingAddress, String zipCode, String englishName, String applicantName, String residenceNumber) {
        this.cardDetail = cardDetail;
        this.homeDetailAddress = homeDetailAddress;
        this.homeAddress = homeAddress;
        this.phoneNumber = phoneNumber;
        this.applicantEmail = applicantEmail;
        this.shippingDetailAddress = shippingDetailAddress;
        this.shippingAddress = shippingAddress;
        this.zipCode = zipCode;
        this.englishName = englishName;
        this.applicantName = applicantName;
        this.residenceNumber = residenceNumber;
        this.cardApplicantInfoId = cardApplicantInfoId;
    }
}