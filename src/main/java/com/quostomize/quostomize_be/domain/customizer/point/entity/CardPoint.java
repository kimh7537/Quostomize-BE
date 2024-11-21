package com.quostomize.quostomize_be.domain.customizer.point.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card_points")
public class CardPoint extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_point_id")
    private Long cardPointId;

    @Column(name = "card_point", nullable = false)
    private Long cardPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_sequence_id", nullable = false)
    private CardDetail cardDetail;

    @Builder
    public CardPoint(Long cardPoint,CardDetail cardDetail){
        super();
        this.cardPoint = cardPoint;
        this.cardDetail=cardDetail;
    }
}