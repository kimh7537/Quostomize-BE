package com.quostomize.quostomize_be.domain.customizer.card.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cards")
public class Card extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "name", length = 17, nullable = false)
    private String name;

    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;

    @Column(name = "brand", nullable = false)
    private Long brand;

    @Column(name = "is_app_card", nullable = false)
    private Boolean isAppCard;

    @Column(name = "is_foreign_blocked", nullable = false)
    private Boolean isForeignBlocked;

    @Column(name = "is_transport", nullable = false)
    private Boolean isTransport;

    @Column(name = "card_password", length = 4, nullable = false)
    private String cardPassword;

    @Column(name = "point", nullable = false)
    private Long point;

    @Column(name = "cvc", length = 3, nullable = false)
    private String cvc;

    @Column(name = "expire_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate expireAt;

    @Column(name = "lotto", nullable = false)
    private Boolean lotto;

    @Column(name = "payback", nullable = false)
    private Boolean payback;

    @Column(name = "piece_stock", nullable = false)
    private Boolean pieceStock;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_type_id", nullable = false)
    private ReceiptMethod receiptMethod;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;
}