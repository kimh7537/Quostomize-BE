package com.quostomize.quostomize_be.domain.customizer.customer.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInterest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_sequence_id", nullable = false)
    private CardDetail cardDetail;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<StockInterest> stockInterests = new ArrayList<>();

    @Builder
    public Customer(Member member, CardDetail cardDetail) {
        this.member = member;
        this.cardDetail = cardDetail;
    }

    public void addStockInterest(StockInterest stockInterest) {
        stockInterests.add(stockInterest);
        stockInterest.updateCustomer(this);
    }
}
