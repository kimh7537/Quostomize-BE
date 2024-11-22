package com.quostomize.quostomize_be.domain.customizer.stock.entity;


import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stock_holdings")
public class StockHolding extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_holding_id")
    private Long stockHoldingId;

    @Column(name = "stock_total_money", nullable = false)
    private Long stockTotalMoney;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_account_id", nullable = false)
    private StockAccount stockAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_information_id", nullable = false)
    private StockInformation stockInformation;

    // StockHolding 클래스에 추가
    @Builder
    public StockHolding(Long stockTotalMoney, StockAccount stockAccount, StockInformation stockInformation) {
        this.stockTotalMoney = stockTotalMoney;
        this.stockAccount = stockAccount;
        this.stockInformation = stockInformation;
    }

    public void updateStockTotalMoney(Long stockTotalMoney) {
        this.stockTotalMoney = stockTotalMoney;
    }
}
