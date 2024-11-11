package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service;

import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;

public interface PointUsageMethodService {
    PointUsageMethod togglePointUsage(Long cardSequenceId, String usageType, boolean isActive);

}
