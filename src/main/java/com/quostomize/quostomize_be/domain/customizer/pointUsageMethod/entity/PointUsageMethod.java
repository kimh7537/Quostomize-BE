package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CardDetail cardDetail;

    public void setIsLotto(boolean isActive) {
    }

    public void setIsPayback(boolean isActive) {
    }

    public void setIsPieceStock(boolean isActive) {
    }
}