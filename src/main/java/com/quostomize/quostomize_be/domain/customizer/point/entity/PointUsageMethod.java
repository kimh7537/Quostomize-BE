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
@Table(name = "point_usage_methods")
public class PointUsageMethod extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_usage_type_id")
    private Long pointUsageTypeId;

    @Column(name = "is_lotto", nullable = false)
    private Boolean isLotto;

    @Column(name = "is_payback", nullable = false)
    private Boolean isPayback;

    @Column(name = "is_piece_stock", nullable = false)
    private Boolean isPieceStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_sequence_id", nullable = false)
    private CardDetail cardDetail;

    @Builder
    public PointUsageMethod(Boolean lotto, Boolean payBack, Boolean pieceStock, CardDetail cardDetail ){
        super();
        this.isLotto = lotto;
        this.isPayback=payBack;
        this.isPieceStock=pieceStock;
        this.cardDetail=cardDetail;
    }
}