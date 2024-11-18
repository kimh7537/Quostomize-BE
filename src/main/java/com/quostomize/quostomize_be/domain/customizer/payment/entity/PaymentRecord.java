package com.quostomize.quostomize_be.domain.customizer.payment.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment_records")
public class PaymentRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_record_id")
    private Long paymentRecordId;

    @Column(name = "industry_type", nullable = false)
    private int industryType;

    @Column(name = "business_registration_number", length = 40, nullable = false)
    private String businessRegistrationNumber;

    @Column(name = "total_payment_amount", nullable = false)
    private Long totalPaymentAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_sequence_id", nullable = false)
    private CardDetail cardDetail;
}