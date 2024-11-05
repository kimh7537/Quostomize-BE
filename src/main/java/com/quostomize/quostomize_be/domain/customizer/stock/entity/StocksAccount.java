package com.quostomize.quostomize_be.domain.customizer.stock;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stocks_accounts")
public class StocksAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_account_id")
    private Long stockAccountId;

    @Column(name = "stock_account_number" )
    private Long stockAccountNumber;

    @Column (name = "stock_account_name")
    private String stockAccountName;

    @Column(name = "stock_account")
    private Boolean stockAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;



}
