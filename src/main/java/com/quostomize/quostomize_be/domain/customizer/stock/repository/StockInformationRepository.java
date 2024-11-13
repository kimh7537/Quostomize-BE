package com.quostomize.quostomize_be.domain.customizer.stock.repository;

import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockInformationRepository extends JpaRepository<StockInformation, Long> {

    Optional<StockInformation> findByStockCode(Integer stockCode);

}
