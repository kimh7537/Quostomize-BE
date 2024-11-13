package com.quostomize.quostomize_be.domain.customizer.stock.repository;

import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInformationRepository extends JpaRepository<StockInformation, Long> {
}
