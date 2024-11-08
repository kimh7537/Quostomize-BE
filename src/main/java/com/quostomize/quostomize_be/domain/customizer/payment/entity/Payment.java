package com.quostomize.quostomize_be.domain.customizer.payment.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.Card;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "payments")
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    Long paymentId;

    @ManyToOne
    @JoinColumn(name = "card_id")
    Card card;

    @Column(name="products")
    String products;

    @Column(name="business_registration_number")
    String businessRegistrationNumber;

    @Builder
    public Payment(Card card, String products, String businessRegistrationNumber) {
        this.card = card;
        this.products = products;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
}
