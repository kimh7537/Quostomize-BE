package com.quostomize.quostomize_be.domain.customizer.payment.service;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.PaymentRecord;
import com.quostomize.quostomize_be.domain.customizer.payment.repository.PaymentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentRecordService {

    private final PaymentRecordRepository paymentRecordRepository;

    public Page<PaymentRecord> getPagedRecords(Pageable pageable) {return paymentRecordRepository.findAll(pageable);}
    public Page<PaymentRecord> getGreaterRecords(Pageable pageable, Long amount) {return paymentRecordRepository.findByPaymentAmountGreater(pageable, amount);}
    public Page<PaymentRecord> getLessRecords(Pageable pageable, Long amount) {return paymentRecordRepository.findByPaymentAmountLess(pageable, amount);}
    public Page<PaymentRecord> getEqualRecords(Pageable pageable, Long amount) {return paymentRecordRepository.findByPaymentAmountEqual(pageable, amount);}

}
