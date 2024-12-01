package com.quostomize.quostomize_be.domain.customizer.payment.strategy;

import com.quostomize.quostomize_be.domain.customizer.payment.enums.RecordSearchType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentSearchFactory {
    private Map<RecordSearchType, PaymentSearchStrategy> strategies;

    @PostConstruct
    public void init() {
        strategies = Map.of(
                RecordSearchType.GREATER, new GreaterSearchStrategy(),
                RecordSearchType.LESS, new LessSearchStrategy(),
                RecordSearchType.EQUAL, new EqualSearchStrategy()
        );
    }

    public PaymentSearchStrategy getStrategy(RecordSearchType type) {
        return strategies.get(type);
    }
}
