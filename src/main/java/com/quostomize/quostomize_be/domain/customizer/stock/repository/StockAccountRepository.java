package com.quostomize.quostomize_be.domain.customizer.stock.repository;

import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockAccountRepository extends JpaRepository<StockAccount, Long> {

    List<StockAccount> findAllByCustomer_CustomerId(Long customerId);
}
