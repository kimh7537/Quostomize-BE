package com.quostomize.quostomize_be.domain.customizer.stock.entity;


import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_account_id", nullable = false)
    private StockAccount stockAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_information_id", nullable = false)
    private StockInformation stockInformation;

}
