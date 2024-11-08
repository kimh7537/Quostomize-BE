package com.quostomize.quostomize_be.domain.customizer.benefit.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "benefit_common_codes")
public class BenefitCommonCode extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benefit_common_id")
    private Long benefitCommonId;

    @Column(name = "benefit_category_type", nullable = false)
    private String benefitCategoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parents_code_id")
    private BenefitCommonCode parentsCode;
}
