package com.quostomize.quostomize_be.domain.customizer.stock.repository;

import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockInformationRepository extends JpaRepository<StockInformation, Long> {

    Optional<StockInformation> findByStockCode(Integer stockCode);

    Optional<StockInformation> findByStockName(String stockName);

    @Query("select si from StockInformation si where si.stockName like %:keyword%")
    List<StockInformation>  findByKeyword(@Param("keyword") String keyword);

}
