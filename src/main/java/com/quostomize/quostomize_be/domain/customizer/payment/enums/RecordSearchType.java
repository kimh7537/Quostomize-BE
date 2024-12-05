package com.quostomize.quostomize_be.domain.customizer.payment.enums;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.PaymentRecord;
import com.quostomize.quostomize_be.domain.customizer.payment.service.PaymentRecordService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum RecordSearchType {
    GREATER("GREATER", "이상") {
        @Override
        public Page<PaymentRecord> search(PaymentRecordService service, Pageable pageable, Long amount) {
            return service.getGreaterRecords(pageable, amount);
        }
    },
    LESS("LESS", "이하") {
        @Override
        public Page<PaymentRecord> search(PaymentRecordService service, Pageable pageable, Long amount) {
            return service.getLessRecords(pageable, amount);
        }
    },
    EQUAL("EQUAL", "동일") {
        @Override
        public Page<PaymentRecord> search(PaymentRecordService service, Pageable pageable, Long amount) {
            return service.getEqualRecords(pageable, amount);
        }
    };

    private final String key;
    private final String description;

    private static final Map<String, RecordSearchType> SEARCH_TYPE_MAP = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(RecordSearchType::getKey, Function.identity()));

    public abstract Page<PaymentRecord> search(PaymentRecordService service, Pageable pageable, Long amount);

    public static RecordSearchType fromKey(final String key) {
        RecordSearchType result = SEARCH_TYPE_MAP.get(key);
        if (result == null) {
            throw new IllegalArgumentException(String.format("요청한 key(%s)를 찾을 수 없습니다.", key));
        }
        return result;
    }
}
