package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service;

import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import org.springframework.stereotype.Service;

public interface PointUsageMethodService {
    // usageType : 포인트 사용방법의 타입(Lotto, Payback, PieceStock)

    PointUsageMethod getPointUsageMethod(Long cardSequenceId);

    PointUsageMethod togglePointUsage(Long cardSequenceId, String usageType, boolean isActive);
}
