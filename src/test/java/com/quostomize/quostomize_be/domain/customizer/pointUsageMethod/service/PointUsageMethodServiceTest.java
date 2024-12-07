package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.LottoService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PointUsageMethodServiceTest {

    @InjectMocks
    private PointUsageMethodService pointUsageMethodService;

    @Mock
    private PointUsageMethodRepository pointUsageMethodRepository;

    @Mock
    private CardDetailRepository cardDetailRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LottoService lottoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test
    @DisplayName("카드 상세 정보가 없는 경우 예외 발생")
    void testGetCardSequenceIdForMember_CardNotFound() {
        // given
        Long memberId = 1L;
        when(customerRepository.findByMember_MemberId(memberId)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                pointUsageMethodService.getCardSequenceIdForMember(memberId));
        assertThat(exception.getMessage()).isEqualTo("Member with ID " + memberId + " does not exist.");

        verify(customerRepository).findByMember_MemberId(memberId);
    }


    @Test
    @DisplayName("옵션 활성화 수 제한(최소 1개) 예외 발생")
    void testMinimumOneOptionRequired() {
        // given
        Long cardSequenceId = 1L;

        CardDetail cardDetail = mock(CardDetail.class);

        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
                .cardDetail(cardDetail)
                .isLotto(false)
                .isPayback(false)
                .isPieceStock(false)
                .build();

        when(pointUsageMethodRepository.findByCardDetail_CardSequenceId(cardSequenceId))
                .thenReturn(Optional.of(pointUsageMethod));

        // when & then
        AppException exception = assertThrows(AppException.class, () ->
                pointUsageMethodService.togglePointUsage(cardSequenceId, "lotto", false));

        assertEquals(ErrorCode.MINIMUM_ONE_OPTION_REQUIRED, exception.getErrorCode());
        verify(pointUsageMethodRepository, never()).save(any());
    }

    @Test
    @DisplayName("페이백과 조각투자를 동시에 활성화 시 예외 발생")
    void testPaybackAndPieceStockConflict() {
        // given
        Long cardSequenceId = 1L;

        CardDetail cardDetail = mock(CardDetail.class);

        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
                .cardDetail(cardDetail)
                .isLotto(false)
                .isPayback(true) // Payback 활성화
                .isPieceStock(false)
                .build();

        when(pointUsageMethodRepository.findByCardDetail_CardSequenceId(cardSequenceId))
                .thenReturn(Optional.of(pointUsageMethod));

        // when & then
        AppException exception = assertThrows(AppException.class, () ->
                pointUsageMethodService.togglePointUsage(cardSequenceId, "piecestock", true));

        assertEquals(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT, exception.getErrorCode());
        verify(pointUsageMethodRepository, never()).save(any());
    }

    @Test
    @DisplayName("포인트 사용 방법 활성화 성공")
    void testTogglePointUsageSuccess() {
        // given
        Long cardSequenceId = 1L;

        CardDetail cardDetail = mock(CardDetail.class);

        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
                .cardDetail(cardDetail)
                .isLotto(false)
                .isPayback(false)
                .isPieceStock(false)
                .build();

        PointUsageMethod updatedPointUsageMethod = PointUsageMethod.builder()
                .cardDetail(cardDetail)
                .isLotto(true)
                .isPayback(false)
                .isPieceStock(false)
                .build();

        when(pointUsageMethodRepository.findByCardDetail_CardSequenceId(cardSequenceId))
                .thenReturn(Optional.of(pointUsageMethod));
        when(pointUsageMethodRepository.save(any(PointUsageMethod.class)))
                .thenReturn(updatedPointUsageMethod);

        // when
        PointUsageMethod result = pointUsageMethodService.togglePointUsage(cardSequenceId, "lotto", true);

        // then
        assertThat(result.getIsLotto()).isTrue();
        assertThat(result.getIsPayback()).isFalse();
        assertThat(result.getIsPieceStock()).isFalse();
        verify(pointUsageMethodRepository).save(any(PointUsageMethod.class));
    }

    @Test
    @DisplayName("페이백, 조각투자 옵션을 동시에 활성화 시 예외 발생")
    void testPaybackAndPieceStockSimultaneousActivation() {
        // given
        Long cardSequenceId = 1L;

        CardDetail cardDetail = mock(CardDetail.class);

        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
                .cardDetail(cardDetail)
                .isLotto(false)
                .isPayback(false)
                .isPieceStock(false)
                .build();

        when(pointUsageMethodRepository.findByCardDetail_CardSequenceId(cardSequenceId))
                .thenReturn(Optional.of(pointUsageMethod));

        // payback 활성화
        PointUsageMethod updatedPointUsageMethod = PointUsageMethod.builder()
                .cardDetail(cardDetail)
                .isLotto(false)
                .isPayback(true)
                .isPieceStock(false)
                .build();

        when(pointUsageMethodRepository.save(any(PointUsageMethod.class)))
                .thenReturn(updatedPointUsageMethod);

        pointUsageMethodService.togglePointUsage(cardSequenceId, "payback", true);

        // when & then - piecestock 활성화 시도
        AppException exception = assertThrows(AppException.class, () ->
                pointUsageMethodService.togglePointUsage(cardSequenceId, "piecestock", true));

        // 예외
        assertEquals(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT, exception.getErrorCode());
        verify(pointUsageMethodRepository, never()).save(any());
    }
}
