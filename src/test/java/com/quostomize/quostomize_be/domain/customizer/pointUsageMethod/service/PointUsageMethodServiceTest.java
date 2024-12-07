package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.LottoService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private CardDetail cardDetail;

    @Mock
    private Customer customer;

    @Test
    @DisplayName("Member ID로 Card Sequence ID 가져오기 성공 테스트")
    void testGetCardSequenceIdForMember_Success() {
        // given
        Long memberId = 1L;
        Long cardSequenceId = 100L;

        when(cardDetail.getCardSequenceId()).thenReturn(cardSequenceId);
        when(customer.getCardDetail()).thenReturn(cardDetail);
        when(customerRepository.findByMember_MemberId(memberId)).thenReturn(Optional.of(customer));

        // when
        long result = pointUsageMethodService.getCardSequenceIdForMember(memberId);

        // then
        assertThat(result).isEqualTo(cardSequenceId);
        verify(customerRepository).findByMember_MemberId(memberId);
    }


    @Test
    @DisplayName("존재하지 않는 Member ID로 예외 발생 테스트")
    void testGetCardSequenceIdForMember_MemberNotFound() {
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
    @DisplayName("CardDetail이 없는 경우 예외 발생 테스트")
    void testGetCardSequenceIdForMember_CardDetailNotFound() {
        // given
        Long memberId = 1L;
        when(customerRepository.findByMember_MemberId(memberId)).thenReturn(Optional.of(customer));
        when(customer.getCardDetail()).thenReturn(null);

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                pointUsageMethodService.getCardSequenceIdForMember(memberId));

        assertThat(exception.getMessage()).isEqualTo("Card details not found for Customer ID 0");
        verify(customerRepository).findByMember_MemberId(memberId);
    }

    @Test
    @DisplayName("최소 1개의 옵션 활성화 제한 테스트")
    void testMinimumOneOptionRequired() {
        // given
        Long cardSequenceId = 1L;
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
    @DisplayName("Payback과 PieceStock 동시 활성화 시 예외 발생 테스트")
    void testPaybackAndPieceStockConflict() {
        // given
        Long cardSequenceId = 1L;
        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
                .cardDetail(cardDetail)
                .isLotto(false)
                .isPayback(true)
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
    @DisplayName("Lotto 옵션 활성화 성공 테스트")
    void testToggleLottoOption_Success() {
        // given
        Long cardSequenceId = 1L;
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
        when(pointUsageMethodRepository.save(any(PointUsageMethod.class))).thenReturn(updatedPointUsageMethod);

        // when
        PointUsageMethod result = pointUsageMethodService.togglePointUsage(cardSequenceId, "lotto", true);

        // then
        assertThat(result.getIsLotto()).isTrue();
        verify(pointUsageMethodRepository).save(any(PointUsageMethod.class));
    }
}

