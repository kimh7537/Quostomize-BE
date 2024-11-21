package com.quostomize.quostomize_be.domain.customizer.benefit.repository;

import com.quostomize.quostomize_be.api.cardapplicant.dto.BenefitHierarchyDTO;

import java.util.List;

public interface BenefitCommonCodeRepositoryCustom {
    List<BenefitHierarchyDTO> findAllBenefitHierarchy();
}
