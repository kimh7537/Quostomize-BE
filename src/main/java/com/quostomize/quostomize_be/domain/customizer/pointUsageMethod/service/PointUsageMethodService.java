package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantRequestDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.LottoService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointUsageMethodService {

    private final PointUsageMethodRepository pointUsageMethodRepository;
    private final LottoService lottoService;
    private final CardDetailRepository cardDetailRepository;

    @Transactional
    public PointUsageMethod getPointUsageMethod(Long cardSequenceId) {
        return pointUsageMethodRepository.findByCardDetail_CardSequenceId(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));
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