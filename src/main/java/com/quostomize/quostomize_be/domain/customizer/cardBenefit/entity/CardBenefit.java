package com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.BenefitCommonCode;
import com.quostomize.quostomize_be.domain.customizer.card.entity.Card;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "card_benefits")
public class CardBenefit extends BaseTimeEntity {

    public CardBenefit() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benefit_id")
    private Long benefitId;

    @Column(name = "benefit_rate", nullable = false)
    private Integer benefitRate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_category_id", nullable = false)
    private BenefitCommonCode upperCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lower_category_id")
    private BenefitCommonCode lowerCategory;
}
