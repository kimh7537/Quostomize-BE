package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodResponse;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.QBenefitCommonCode;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.QBenefitFranchiseRelationship;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.QFranchise;
import com.quostomize.quostomize_be.domain.customizer.card.entity.QCardDetail;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.QCardBenefit;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.QPointUsageMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointUsageMethodRepositoryImpl implements PointUsageMethodRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public List<PointUsageMethodResponse> findMyCardDetails(Long cardSequenceId) {
        QPointUsageMethod pum = QPointUsageMethod.pointUsageMethod;
        QCardDetail cd = QCardDetail.cardDetail;
        QCardBenefit cb = QCardBenefit.cardBenefit;
        QBenefitCommonCode upperCode = new QBenefitCommonCode("upperCode");
        QBenefitCommonCode lowerCode = new QBenefitCommonCode("lowerCode");
        QBenefitFranchiseRelationship bfr = QBenefitFranchiseRelationship.benefitFranchiseRelationship;
        QFranchise f = QFranchise.franchise;

        return queryFactory
                .select(Projections.constructor(
                        PointUsageMethodResponse.class,
                        pum.pointUsageTypeId,
                        pum.isLotto,
                        pum.isPayback,
                        pum.isPieceStock,
                        cd.cardSequenceId,
                        cd.cardColor,
                        cd.cardNumber,
                        cb.benefitId,
                        cb.benefitRate,
                        cb.isActive,
                        upperCode.benefitCategoryType,
                        lowerCode.benefitCategoryType,
                        f.franchiseName
                ))
                .from(pum)
                .join(pum.cardDetail, cd)
                .join(cb).on(cb.cardDetail.eq(cd).and(cb.isActive.eq(true)))
                .leftJoin(cb.upperCategory, upperCode)
                .leftJoin(cb.lowerCategory, lowerCode)
                .leftJoin(bfr).on(bfr.benefitCommonCode.eq(upperCode)
                        .or(bfr.benefitCommonCode.eq(lowerCode)))
                .leftJoin(bfr.franchise, f)
                .where(pum.cardDetail.cardSequenceId.eq(cardSequenceId))
                .fetch();
    }

}
