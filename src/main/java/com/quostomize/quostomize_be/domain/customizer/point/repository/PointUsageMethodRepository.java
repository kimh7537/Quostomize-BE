package com.quostomize.quostomize_be.domain.customizer.point.repository;

import com.quostomize.quostomize_be.domain.customizer.point.entity.PointUsageMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointUsageMethodRepository extends JpaRepository<PointUsageMethod, Long> {
}
