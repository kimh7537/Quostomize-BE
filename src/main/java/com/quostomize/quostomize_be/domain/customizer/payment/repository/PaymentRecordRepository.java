package com.quostomize.quostomize_be.domain.customizer.payment.repository;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.PaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Page<PaymentRecord> findAll(Pageable pageable);
    @Query("select pr from PaymentRecord pr where pr.totalPaymentAmount >= :amount")
    Page<PaymentRecord> findByPaymentAmountGreater(Pageable pageable, @Param("amount") Long amount);
    @Query("select pr from PaymentRecord pr where pr.totalPaymentAmount <= :amount")
    Page<PaymentRecord> findByPaymentAmountLess(Pageable pageable, @Param("amount") Long amount);
    @Query("select pr from PaymentRecord pr where pr.totalPaymentAmount = :amount")
    Page<PaymentRecord> findByPaymentAmountEqual(Pageable pageable, @Param("amount") Long amount);
}
