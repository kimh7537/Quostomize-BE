package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantRequestDto;
import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PointUsageMethodService {

    private final PointUsageMethodRepository pointUsageMethodRepository;
    private final LottoService lottoService;
    private final CardDetailRepository cardDetailRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public List<PointUsageMethodResponse> getMyCardDetails(Long memberId) {
        long cardSequenceId = getCardSequenceIdForMember(memberId);
        log.info("cardSequenceId: {}", cardSequenceId);
        return pointUsageMethodRepository.findMyCardDetails(cardSequenceId);
    }

    @Transactional
    public PointUsageMethod getPointUsageMethod(Long memberId) {
        long cardSequenceId = getCardSequenceIdForMember(memberId);
        return pointUsageMethodRepository.findByCardDetail_CardSequenceId(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));
    }

    private long getCardSequenceIdForMember(Long memberId) {
        return customerRepository.findByMember_MemberId(memberId)
                .map(customer -> {
                    if (customer.getCardDetail() == null) {
                        throw new EntityNotFoundException("Card details not found for Customer ID " + customer.getCustomerId());
                    }
                    return customer.getCardDetail().getCardSequenceId();
                })
                .orElseThrow(() -> new EntityNotFoundException("Member with ID " + memberId + " does not exist."));
    }

    @Transactional
    public PointUsageMethod togglePointUsage(Long cardSequenceId, String usageType, boolean isActive) {
        // 로또 관련 처리는 LottoService로 위임
        if ("lotto".equals(usageType)) {
            return handleLottoToggle(cardSequenceId, isActive);
        }

        // 나머지 포인트 사용 방법 처리
        return handleOtherPointUsage(cardSequenceId, usageType, isActive);
    }

    private PointUsageMethod handleLottoToggle(Long cardSequenceId, boolean isActive) {
        LottoParticipantRequestDto request = new LottoParticipantRequestDto(cardSequenceId, isActive);
        lottoService.toggleLottoParticipation(request);
        return getPointUsageMethod(cardSequenceId);
    }

    private PointUsageMethod handleOtherPointUsage(Long cardSequenceId, String usageType, boolean isActive) {
        PointUsageMethod pointUsageMethod = getPointUsageMethod(cardSequenceId);

        PointUsageMethod.PointUsageMethodBuilder builder = PointUsageMethod.builder()
                .pointUsageTypeId(pointUsageMethod.getPointUsageTypeId())
                .isLotto(pointUsageMethod.getIsLotto())
                .isPayback(pointUsageMethod.getIsPayback())
                .isPieceStock(pointUsageMethod.getIsPieceStock())
                .cardDetail(pointUsageMethod.getCardDetail());

        // payback과 piecestock 처리
        if ("payback".equals(usageType)) {
            if (isActive && pointUsageMethod.getIsPieceStock()) {
                throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
            }
            builder.isPayback(isActive);
        } else if ("piecestock".equals(usageType)) {
            if (isActive && pointUsageMethod.getIsPayback()) {
                throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
            }
            builder.isPieceStock(isActive);
        }

        PointUsageMethod updatedPointUsage = builder.build();
        validateActiveOptions(updatedPointUsage);

        return pointUsageMethodRepository.save(updatedPointUsage);
    }

    private void validateActiveOptions(PointUsageMethod pointUsageMethod) {
        int activeCount = (pointUsageMethod.getIsLotto() ? 1 : 0) +
                (pointUsageMethod.getIsPayback() ? 1 : 0) +
                (pointUsageMethod.getIsPieceStock() ? 1 : 0);

        if (activeCount < 1) {
            throw new AppException(ErrorCode.MINIMUM_ONE_OPTION_REQUIRED);
        }
        if (activeCount > 2) {
            throw new AppException(ErrorCode.MAXIMUM_TWO_OPTIONS_ALLOWED);
        }
    }

    @Transactional
    public PointUsageMethod createPointUsageMethod(Long cardId, Boolean stock, Boolean lotto, Boolean payback) {
        // 카드 정보 조회
        CardDetail cardDetail = cardDetailRepository.findById(cardId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));

        // 페이백과 조각투자 동시 선택 검증
        if (payback && stock) {
            throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
        }

        // 포인트 사용 방법 생성
        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
                .isLotto(lotto)
                .isPayback(payback)
                .isPieceStock(stock)
                .cardDetail(cardDetail)
                .build();

        // 활성화된 옵션 수 검증
        validateActiveOptions(pointUsageMethod);

        // 저장 및 반환
        return pointUsageMethodRepository.save(pointUsageMethod);
    }
}