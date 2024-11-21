package com.quostomize.quostomize_be.domain.customizer.benefit.repository;

import com.quostomize.quostomize_be.domain.customizer.benefit.entity.BenefitCommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitCommonCodeRepository extends JpaRepository<BenefitCommonCode, Long>, BenefitCommonCodeRepositoryCustom {
}
