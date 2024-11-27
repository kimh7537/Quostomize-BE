package com.quostomize.quostomize_be.domain.customizer.benefit.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.quostomize.quostomize_be.api.cardapplicant.dto.BenefitHierarchyDTO;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.QBenefitCommonCode;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.QBenefitFranchiseRelationship;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.QFranchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BenefitCommonCodeRepositoryImpl implements BenefitCommonCodeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BenefitHierarchyDTO> findAllBenefitHierarchy() {
        QBenefitCommonCode bc = QBenefitCommonCode.benefitCommonCode;
        QBenefitFranchiseRelationship bfr = QBenefitFranchiseRelationship.benefitFranchiseRelationship;
        QFranchise f = QFranchise.franchise;

        return queryFactory
                .selectDistinct(Projections.constructor(BenefitHierarchyDTO.class,
                        bc.benefitCommonId,
                        bc.benefitCategoryType,
                        bc.parentsCode.benefitCommonId,
                        f.franchiseName))
                .from(bc)
                .leftJoin(bfr).on(bc.benefitCommonId.eq(bfr.benefitCommonCode.benefitCommonId))
                .leftJoin(f).on(f.franchiseId.eq(bfr.franchise.franchiseId))
                .orderBy(bc.benefitCommonId.asc())
                .fetch();
    }
}
