package com.quostomize.quostomize_be.domain.customizer.customer.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.card.entity.Card;
import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customer_id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    // TODO: card id 수정 필요
    @OneToOne
    @JoinColumn(name = "id")
    private Card card;
}
