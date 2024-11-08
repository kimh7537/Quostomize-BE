package com.quostomize.quostomize_be.domain.customizer.payment.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "lotto_participants",
        uniqueConstraints = {@UniqueConstraint(name = "UniqueCard", columnNames = {"card"})}
)
public class LottoParticipant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lotto_participant_id")
    private Long lottoParticipantId;

    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;
}
