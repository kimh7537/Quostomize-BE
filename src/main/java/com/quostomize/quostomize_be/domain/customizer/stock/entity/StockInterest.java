package com.quostomize.quostomize_be.domain.customizer.stock.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stock_interests")
public class StockInterest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_interest_id")
    private Long stockInterestId;

    @Column(name = "priority")
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_information_id")
    private StockInformation stockInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Builder
    public StockInterest(Integer priority, StockInformation stockInformation, Customer customer) {
        this.priority = priority;
        this.stockInformation = stockInformation;
        this.customer = customer;
    }

    public void updateCustomer(Customer customer){
        this.customer = customer;
    }
}