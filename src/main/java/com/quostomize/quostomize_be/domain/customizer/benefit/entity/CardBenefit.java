package com.quostomize.quostomize_be.domain.customizer.benefit.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card_benefits")
public class CardBenefit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benefit_id")
    private Long benefitId;

    @Column(name = "benefit_rate", nullable = false)
    private Integer benefitRate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_sequence_id", nullable = false)
    private CardDetail cardDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_category_id", nullable = false)
    private BenefitCommonCode upperCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lower_category_id")
    private BenefitCommonCode lowerCategory;
}
