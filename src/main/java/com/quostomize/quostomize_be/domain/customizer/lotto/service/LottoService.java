package com.quostomize.quostomize_be.domain.customizer.lotto.service;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantRequestDto;
import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantResponseDto;
import com.quostomize.quostomize_be.api.point.dto.PointUsageMethodRequestDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LottoService {

    private final DailyLottoParticipantRepository dailyLottoParticipantRepository;
    private final PointUsageMethodRepository pointUsageMethodRepository;
    private final CardPointRepository cardPointRepository;
    private final CustomerRepository customerRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String LOTTO_PARTICIPANTS_COUNT_KEY = "lottoParticipantsCount";
    private static final int LOTTO_MINIMUM_POINTS = 10;

    // 로또 참여 조건에 맞는 사용자 대해 로또 참여 처리
    @Transactional
    @CacheEvict(value = "lottoParticipantsCount", allEntries = true)
    public List<LottoParticipantResponseDto> registerLottoParticipants() {
        return pointUsageMethodRepository.findAllByIsLottoTrue().stream()
                .map(this::processPointUsageMethod)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // 사용자의 로또 참여 설정을 ON/OFF 하고, 참여자를 추가하거나 제거
    @Transactional
    @CacheEvict(value = "lottoParticipantsCount", allEntries = true)
    public void toggleLottoParticipation(LottoParticipantRequestDto request) {
        PointUsageMethod pointUsageMethod = pointUsageMethodRepository
                .findByCardDetail_CardSequenceId(request.cardSequenceId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        PointUsageMethodRequestDto dto = PointUsageMethodRequestDto.from(pointUsageMethod, request);
        PointUsageMethod updatedPointUsageMethod = dto.toEntity();

        Consumer<Long> action = request.isLottoOn()
                ? this::addParticipantIfEligible
                : this::removeParticipantIfExists;

        action.accept(request.cardSequenceId());
        pointUsageMethodRepository.save(updatedPointUsageMethod);
    }

    // 개별 포인트 사용 방법에 대해 로또 참여 가능성을 처리
    private Optional<LottoParticipantResponseDto> processPointUsageMethod(PointUsageMethod pointUsageMethod) {
        Long cardSequenceId = pointUsageMethod.getCardDetail().getCardSequenceId();

        Optional<CardPoint> cardPointOptional = cardPointRepository.findByCardDetail_CardSequenceId(cardSequenceId);

        if (cardPointOptional.isPresent() && isEligibleForLotto(cardPointOptional.get())) {
            Optional<Customer> customerOptional = customerRepository.findByCardDetail_CardSequenceId(cardSequenceId);
            return customerOptional.flatMap(this::registerCustomerForLotto);
        }
        return Optional.empty();
    }

    // 고객의 로또 참여를 등록하고, 이미 등록된 경우 빈 Optional을 반환
    private Optional<LottoParticipantResponseDto> registerCustomerForLotto(Customer customer) {
        if (!isCustomerAlreadyRegistered(customer)) {
            DailyLottoParticipant participant = DailyLottoParticipant.builder()
                    .customer(customer)
                    .build();
            dailyLottoParticipantRepository.save(participant);
            incrementLottoParticipantsCount();
            return Optional.of(new LottoParticipantResponseDto(customer.getCustomerId(), "Successfully registered for lotto"));
        }
        return Optional.empty();
    }

    // 카드 포인트가 로또 참여 최소 포인트 이상인지 확인
    private boolean isEligibleForLotto(CardPoint cardPoint) {
        return cardPoint != null && cardPoint.getCardPoint() >= LOTTO_MINIMUM_POINTS;
    }

    // 고객이 이미 로또에 등록되었는지 확인
    private boolean isCustomerAlreadyRegistered(Customer customer) {
        return dailyLottoParticipantRepository.findByCustomer(customer).isPresent();
    }

    // 총 로또 참여자 수를 Redis 또는 데이터베이스에서 조회
    @Cacheable(value = "lottoParticipantsCount")
    public Long getTotalLottoParticipantsCount() {
        String countValue = redisTemplate.opsForValue().get(LOTTO_PARTICIPANTS_COUNT_KEY);
        return (countValue != null) ? Long.parseLong(countValue) : dailyLottoParticipantRepository.count();
    }

    // 카드 ID에 해당하는 고객이 로또 참여 조건을 충족하면 오늘 참여자 추가
    private void addParticipantIfEligible(Long cardSequenceId) {
        Optional<CardPoint> cardPointOptional = cardPointRepository.findByCardDetail_CardSequenceId(cardSequenceId);

        if (cardPointOptional.isPresent() && isEligibleForLotto(cardPointOptional.get())) {
            Optional<Customer> customerOptional = customerRepository.findByCardDetail_CardSequenceId(cardSequenceId);

            customerOptional.ifPresent(customer -> {
                if (!isCustomerAlreadyRegistered(customer)) {
                    DailyLottoParticipant participant = DailyLottoParticipant.builder()
                            .customer(customer)
                            .build();
                    dailyLottoParticipantRepository.save(participant);
                    incrementLottoParticipantsCount();
                }
            });
        }
    }

    // 카드 ID에 해당하는 고객의 로또 오늘 참여자에서 제거
    private void removeParticipantIfExists(Long cardSequenceId) {
        Optional<Customer> customerOptional = customerRepository.findByCardDetail_CardSequenceId(cardSequenceId);

        customerOptional.ifPresent(customer -> {
            Optional<DailyLottoParticipant> existingParticipant = dailyLottoParticipantRepository.findByCustomer(customer);
            existingParticipant.ifPresent(participant -> {
                dailyLottoParticipantRepository.delete(participant);
                decrementLottoParticipantsCount();
            });
        });
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
