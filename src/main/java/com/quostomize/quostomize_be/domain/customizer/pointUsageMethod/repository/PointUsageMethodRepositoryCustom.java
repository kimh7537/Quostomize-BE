package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository;

import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodResponse;

import java.util.List;

public interface PointUsageMethodRepositoryCustom {
    List<PointUsageMethodResponse> findMyCardDetails(Long cardSequenceId);
}
