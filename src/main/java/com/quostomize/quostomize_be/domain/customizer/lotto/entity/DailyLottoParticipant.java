package com.quostomize.quostomize_be.domain.customizer.lotto.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "daily_lotto_participant")
public class DailyLottoParticipant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_lotto_participant_id")
    private Long dailyLottoParticipantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
