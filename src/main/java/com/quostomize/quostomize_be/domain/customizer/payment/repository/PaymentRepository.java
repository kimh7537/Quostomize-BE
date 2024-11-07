package com.quostomize.quostomize_be.domain.customizer.payment.repository;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
