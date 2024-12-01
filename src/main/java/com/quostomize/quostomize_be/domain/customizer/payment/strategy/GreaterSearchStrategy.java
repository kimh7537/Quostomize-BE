package com.quostomize.quostomize_be.domain.customizer.payment.strategy;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.PaymentRecord;
import com.quostomize.quostomize_be.domain.customizer.payment.service.PaymentRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class GreaterSearchStrategy implements PaymentSearchStrategy {
    @Override
    public Page<PaymentRecord> search(PaymentRecordService service, Pageable pageable, Long searchAmount) {
        return service.getGreaterRecords(pageable, searchAmount);
    }
}
