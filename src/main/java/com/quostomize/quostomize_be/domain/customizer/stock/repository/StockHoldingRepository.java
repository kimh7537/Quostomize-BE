package com.quostomize.quostomize_be.domain.customizer.stock.repository;

import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockAccount;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockHolding;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockHoldingRepository extends JpaRepository<StockHolding, Long> {
    Optional<StockHolding> findByStockAccountAndStockInformation(StockAccount stockAccount, StockInformation stockInformation);
}
