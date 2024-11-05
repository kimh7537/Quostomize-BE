package com.quostomize.quostomize_be.domain.customizer.benefit.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "benefit_franchise_relationships")
public class BenefitFranchiseRelationship extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Long relationshipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id", nullable = false)
    private Franchise franchise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lower_category_id", nullable = false)
    private BenefitCommonCode lowerCategory;
}
