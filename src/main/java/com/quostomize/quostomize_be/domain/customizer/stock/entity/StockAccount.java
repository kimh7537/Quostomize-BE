package com.quostomize.quostomize_be.domain.customizer.stock.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stock_accounts")
public class StockAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_account_id")
    private Long stockAccountId;

    @Column(name = "stock_account_number", nullable = false)
    private Long stockAccountNumber;

    @Column(name = "stock_account_name", length = 30, nullable = false)
    private String stockAccountName;

    @Column(name = "is_stock_account_active", nullable = false)
    private Boolean isStockAccountActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Lob
    @Column(name = "stock_access_token",columnDefinition = "TEXT")
    private String openAPIToken;

    @Column(name = "access_token_expiry")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expiryDate;

    @Builder
    public StockAccount(Long stockAccountNumber, String stockAccountName, Boolean isStockAccountActive, Customer customer, String openAPIToken, LocalDateTime expiryDate){
        this.stockAccountNumber = stockAccountNumber;
        this.stockAccountName = stockAccountName;
        this.isStockAccountActive = isStockAccountActive;
        this.customer = customer;
        this.openAPIToken = openAPIToken;
        this.expiryDate = expiryDate;
    }

    public void updateStockAccountActive(boolean isStockAccountActive){
        this.isStockAccountActive = isStockAccountActive;
    }

    public void updateAccessTokenInfo(String openAPIToken, LocalDateTime expiryDate) {
        this.openAPIToken = openAPIToken;
        this.expiryDate = expiryDate;
    }
}
