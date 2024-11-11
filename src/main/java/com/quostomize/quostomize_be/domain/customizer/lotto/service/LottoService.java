package com.quostomize.quostomize_be.domain.customizer.lotto.service;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantRequestDto;
import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantResponseDto;
import com.quostomize.quostomize_be.api.point.dto.PointUsageMethodRequestDto;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.customizer.lotto.entity.DailyLottoParticipant;
import com.quostomize.quostomize_be.domain.customizer.lotto.repository.DailyLottoParticipantRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import com.quostomize.quostomize_be.domain.customizer.point.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.point.repository.CardPointRepository;
import com.quostomize.quostomize_be.domain.customizer.point.repository.PointUsageMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final DailyLottoParticipantRepository dailyLottoParticipantRepository;
    private final PointUsageMethodRepository pointUsageMethodRepository;
    private final CardPointRepository cardPointRepository;
    private final CustomerRepository customerRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String LOTTO_PARTICIPANTS_COUNT_KEY = "lottoParticipantsCount";

    @Transactional
    @CacheEvict(value = "lottoParticipantsCount", allEntries = true)
    public List<LottoParticipantResponseDto> registerLottoParticipants() {
        List<PointUsageMethod> pointUsageMethods = pointUsageMethodRepository.findAllByIsLottoTrue();

        return pointUsageMethods.stream()
                .map(this::processPointUsageMethod)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "lottoParticipantsCount", allEntries = true)
    public void toggleLottoParticipation(LottoParticipantRequestDto request) {
        PointUsageMethod pointUsageMethod = pointUsageMethodRepository.findByCardDetail_CardSequenceId(request.cardSequenceId());

        if (pointUsageMethod != null) {
            PointUsageMethodRequestDto dto = PointUsageMethodRequestDto.from(pointUsageMethod, request);
            PointUsageMethod updatedPointUsageMethod = dto.toEntity();

            if (request.isLottoOn()) {
                addParticipantIfEligible(request.cardSequenceId());
            } else {
                removeParticipantIfExists(request.cardSequenceId());
            }
            pointUsageMethodRepository.save(updatedPointUsageMethod);
        }
    }

    private Optional<LottoParticipantResponseDto> processPointUsageMethod(PointUsageMethod pointUsageMethod) {
        Long cardSequenceId = pointUsageMethod.getCardDetail().getCardSequenceId();
        CardPoint cardPoint = cardPointRepository.findByCardDetail_CardSequenceId(cardSequenceId);

        if (isEligibleForLotto(cardPoint)) {
            Customer customer = customerRepository.findByCardDetail_CardSequenceId(cardSequenceId);
            return registerCustomerForLotto(customer);
        }
        return Optional.empty();
    }

    private Optional<LottoParticipantResponseDto> registerCustomerForLotto(Customer customer) {
        if (customer != null && !isCustomerAlreadyRegistered(customer)) {
            DailyLottoParticipant participant = DailyLottoParticipant.builder()
                    .customer(customer)
                    .build();
            dailyLottoParticipantRepository.save(participant);
            incrementLottoParticipantsCount();
            return Optional.of(new LottoParticipantResponseDto(customer.getCustomerId(), "Successfully registered for lotto"));
        }
        return Optional.empty();
    }

    private boolean isEligibleForLotto(CardPoint cardPoint) {
        return cardPoint != null && cardPoint.getCardPoint() >= 10;
    }

    private boolean isCustomerAlreadyRegistered(Customer customer) {
        return dailyLottoParticipantRepository.findByCustomer(customer).isPresent();
    }

    @Cacheable(value = "lottoParticipantsCount")
    public Long getTotalLottoParticipantsCount() {
        String countValue = redisTemplate.opsForValue().get(LOTTO_PARTICIPANTS_COUNT_KEY);
        return (countValue != null) ? Long.parseLong(countValue) : dailyLottoParticipantRepository.count();
    }

    private void addParticipantIfEligible(Long cardSequenceId) {
        CardPoint cardPoint = cardPointRepository.findByCardDetail_CardSequenceId(cardSequenceId);
        if (isEligibleForLotto(cardPoint)) {
            Customer customer = customerRepository.findByCardDetail_CardSequenceId(cardSequenceId);
            if (customer != null && !isCustomerAlreadyRegistered(customer)) {
                DailyLottoParticipant participant = DailyLottoParticipant.builder()
                        .customer(customer)
                        .build();
                dailyLottoParticipantRepository.save(participant);
                incrementLottoParticipantsCount();
            }
        }
    }

    private void removeParticipantIfExists(Long cardSequenceId) {
        Customer customer = customerRepository.findByCardDetail_CardSequenceId(cardSequenceId);
        if (customer != null) {
            Optional<DailyLottoParticipant> existingParticipant = dailyLottoParticipantRepository.findByCustomer(customer);
            existingParticipant.ifPresent(participant -> {
                dailyLottoParticipantRepository.delete(participant);
                decrementLottoParticipantsCount();
            });
        }
    }

    private void incrementLottoParticipantsCount() {
        String countValue = redisTemplate.opsForValue().get(LOTTO_PARTICIPANTS_COUNT_KEY);
        Long currentCount = (countValue != null) ? Long.parseLong(countValue) : 0;
        redisTemplate.opsForValue().set(LOTTO_PARTICIPANTS_COUNT_KEY, String.valueOf(currentCount + 1));
    }

    private void decrementLottoParticipantsCount() {
        String countValue = redisTemplate.opsForValue().get(LOTTO_PARTICIPANTS_COUNT_KEY);
        Long currentCount = (countValue != null) ? Long.parseLong(countValue) : 0;
        if (currentCount > 0) {
            redisTemplate.opsForValue().set(LOTTO_PARTICIPANTS_COUNT_KEY, String.valueOf(currentCount - 1));
        }
    }
}
