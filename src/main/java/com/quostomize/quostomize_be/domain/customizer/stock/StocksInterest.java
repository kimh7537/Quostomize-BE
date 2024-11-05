package com.quostomize.quostomize_be.domain.customizer.stock;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stocks_interests")
public class StocksInterest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_interest_id")
    private Long stockInterestId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_information_id")
    private StocksInfo stocksInfo;

    @Column(name = "priority")
    private Integer priority;


}
