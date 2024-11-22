package com.quostomize.quostomize_be.domain.customizer.card.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card_details", uniqueConstraints = {@UniqueConstraint(name = "CARD_NUMBER_UNIQUE", columnNames = {"card_number"})})
public class CardDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_sequence_id")
    private Long cardSequenceId;

    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;

    @Column(name = "card_color", nullable = false)
    private int cardColor;

    @Column(name = "card_brand", nullable = false)
    private int cardBrand;

    @Column(name = "is_app_card", nullable = false)
    private Boolean isAppCard;

    @Column(name = "is_foreign_blocked", nullable = false)
    private Boolean isForeignBlocked;

    @Column(name = "is_postpaid_transport", nullable = false)
    private Boolean isPostpaidTransport;

    @Column(name = "card_password", nullable = false)
    private String cardPassword;

    @Column(name = "cvc_number", nullable = false)
    private String cvcNumber;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "optional_terms", nullable = false)
    private int optionalTerms;

    @Column(name = "payment_receipt_methods", nullable = false)
    private int paymentReceiptMethods;

    @Builder
    public CardDetail(int paymentReceiptMethods, int optionalTerms, LocalDate expirationDate, String cvcNumber, String cardPassword, Boolean isPostpaidTransport, Boolean isForeignBlocked, Boolean isAppCard, int cardBrand, int cardColor, String cardNumber, long cardSequenceId) {
        this.paymentReceiptMethods = paymentReceiptMethods;
        this.optionalTerms = optionalTerms;
        this.expirationDate = expirationDate;
        this.cvcNumber = cvcNumber;
        this.cardPassword = cardPassword;
        this.isPostpaidTransport = isPostpaidTransport;
        this.isForeignBlocked = isForeignBlocked;
        this.isAppCard = isAppCard;
        this.cardBrand = cardBrand;
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;
        this.cardSequenceId = cardSequenceId;
    }

}