package com.quostomize.quostomize_be.domain.customizer.lotto.service;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantRequestDto;
import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantResponseDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.customizer.lotto.entity.DailyLottoParticipant;
import com.quostomize.quostomize_be.domain.customizer.lotto.repository.DailyLottoParticipantRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import com.quostomize.quostomize_be.domain.customizer.point.repository.CardPointRepository;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LottoServiceTest {

    @InjectMocks
    private LottoService lottoService;

    @Mock
    private DailyLottoParticipantRepository dailyLottoParticipantRepository;

    @Mock
    private PointUsageMethodRepository pointUsageMethodRepository;

    @Mock
    private CardPointRepository cardPointRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

//    @BeforeEach
//    void setUp() {
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//    }

    @Test
    @DisplayName("로또 참여자 등록 - 성공")
    void registerLottoParticipants_Success() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        CardDetail cardDetail = mock(CardDetail.class);
        when(cardDetail.getCardSequenceId()).thenReturn(1L);

        PointUsageMethod pointUsageMethod = mock(PointUsageMethod.class);
        when(pointUsageMethod.getCardDetail()).thenReturn(cardDetail);
        when(pointUsageMethodRepository.findAllByIsLottoTrue()).thenReturn(List.of(pointUsageMethod));

        CardPoint cardPoint = mock(CardPoint.class);
        when(cardPoint.getCardPoint()).thenReturn(15L);
        when(cardPointRepository.findByCardDetail_CardSequenceId(anyLong())).thenReturn(Optional.of(cardPoint));

        Customer customer = mock(Customer.class);
        when(customer.getCustomerId()).thenReturn(1L);
        when(customerRepository.findByCardDetail_CardSequenceId(anyLong())).thenReturn(Optional.of(customer));

        when(dailyLottoParticipantRepository.findByCustomer(any())).thenReturn(Optional.empty());

        // when
        List<LottoParticipantResponseDto> result = lottoService.registerLottoParticipants();

        // then
        assertFalse(result.isEmpty());
        verify(dailyLottoParticipantRepository).save(any(DailyLottoParticipant.class));
        verify(valueOperations).set(eq("lottoParticipantsCount"), anyString());
    }

    @Test
    @DisplayName("로또 참여 설정 변경 - 참여 ON")
    void toggleLottoParticipation_TurnOn_Success() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        LottoParticipantRequestDto request = new LottoParticipantRequestDto(1L, true);

        PointUsageMethod pointUsageMethod = mock(PointUsageMethod.class);
        when(pointUsageMethodRepository.findByCardDetail_CardSequenceId(anyLong()))
                .thenReturn(Optional.of(pointUsageMethod));

        CardPoint cardPoint = mock(CardPoint.class);
        when(cardPoint.getCardPoint()).thenReturn(15L);
        when(cardPointRepository.findByCardDetail_CardSequenceId(anyLong())).thenReturn(Optional.of(cardPoint));

        Customer customer = mock(Customer.class);
        when(customerRepository.findByCardDetail_CardSequenceId(anyLong())).thenReturn(Optional.of(customer));

        when(dailyLottoParticipantRepository.findByCustomer(any())).thenReturn(Optional.empty());

        // when
        lottoService.toggleLottoParticipation(request);

        // then
        verify(dailyLottoParticipantRepository).save(any(DailyLottoParticipant.class));
        verify(valueOperations).set(eq("lottoParticipantsCount"), anyString());
    }

    @Test
    @DisplayName("로또 참여 설정 변경 - 참여 OFF")
    void toggleLottoParticipation_TurnOff_Success() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        LottoParticipantRequestDto request = new LottoParticipantRequestDto(1L, false);

        PointUsageMethod pointUsageMethod = mock(PointUsageMethod.class);
        when(pointUsageMethodRepository.findByCardDetail_CardSequenceId(anyLong()))
                .thenReturn(Optional.of(pointUsageMethod));

        Customer customer = mock(Customer.class);
        when(customerRepository.findByCardDetail_CardSequenceId(anyLong())).thenReturn(Optional.of(customer));

        DailyLottoParticipant participant = mock(DailyLottoParticipant.class);
        when(dailyLottoParticipantRepository.findByCustomer(any())).thenReturn(Optional.of(participant));

        // 카운트 감소를 위한 모킹 추가
        when(valueOperations.get("lottoParticipantsCount")).thenReturn("5");

        // when
        lottoService.toggleLottoParticipation(request);

        // then
        verify(dailyLottoParticipantRepository).delete(any(DailyLottoParticipant.class));
        verify(valueOperations).set(eq("lottoParticipantsCount"), eq("4"));
    }

    @Test
    @DisplayName("로또 참여 설정 변경 - 포인트 사용 방법 없음")
    void toggleLottoParticipation_PointUsageMethodNotFound() {
        // given
        LottoParticipantRequestDto request = new LottoParticipantRequestDto(1L, true);
        when(pointUsageMethodRepository.findByCardDetail_CardSequenceId(anyLong()))
                .thenReturn(Optional.empty());

        // when, then
        AppException exception = assertThrows(AppException.class,
                () -> lottoService.toggleLottoParticipation(request));

        assertEquals(ErrorCode.ENTITY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("총 로또 참여자 수 조회 - Redis 값 있음")
    void getTotalLottoParticipantsCount_WithRedisValue() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("lottoParticipantsCount")).thenReturn("10");

        // when
        Long count = lottoService.getTotalLottoParticipantsCount();

        // then
        assertEquals(10L, count);
    }

    @Test
    @DisplayName("총 로또 참여자 수 조회 - Redis 값 없음")
    void getTotalLottoParticipantsCount_WithoutRedisValue() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("lottoParticipantsCount")).thenReturn(null);
        when(dailyLottoParticipantRepository.count()).thenReturn(5L);

        // when
        Long count = lottoService.getTotalLottoParticipantsCount();

        // then
        assertEquals(5L, count);
    }
}