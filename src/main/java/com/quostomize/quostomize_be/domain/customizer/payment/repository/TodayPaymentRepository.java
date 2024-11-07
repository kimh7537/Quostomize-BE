package com.quostomize.quostomize_be.domain.customizer.payment.repository;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.TodayPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayPaymentRepository extends JpaRepository<TodayPayment, Long> {
}
