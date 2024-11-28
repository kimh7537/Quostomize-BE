package com.quostomize.quostomize_be.domain.customizer.payment.repository;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.PaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Page<PaymentRecord> findAll(Pageable pageable);
}
