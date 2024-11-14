package com.quostomize.quostomize_be.domain.customizer.stock.entity;


import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
