package com.quostomize.quostomize_be.domain.customizer.stock;


import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stocks_holdings")
public class StocksHolding extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_holdings")
    private Long stockHoldings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_account_id")
    private StocksAccount stocksAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_information_id")
    private StocksInfo stocksInfo;
}
