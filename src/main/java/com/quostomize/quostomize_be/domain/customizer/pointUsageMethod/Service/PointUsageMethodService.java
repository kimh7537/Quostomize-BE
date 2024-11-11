package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service;

import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;

public interface PointUsageMethodService {
    PointUsageMethod togglePointUsage(Long cardSequenceId, String usageType, boolean isActive);
 // usageType : 포인트 사용방법의 타입(Lotto, Payback, PieceStock)

}
