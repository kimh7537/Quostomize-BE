package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service;

import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodRequestDto;
import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodResponseDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointUsageMethodService {

    private final PointUsageMethodRepository repository;

    @Transactional
    public PointUsageMethod getPointUsageMethod(Long cardSequenceId) {
        return repository.findByCardDetail_CardSequenceId(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));
    }

    @Transactional
    public PointUsageMethod togglePointUsage(Long cardSequenceId, String usageType, boolean isActive) {
        PointUsageMethod pointUsageMethod = repository.findByCardDetail_CardSequenceId(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));

        // 기본 빌더 생성
        PointUsageMethod.PointUsageMethodBuilder updatedPointUsageMethod = pointUsageMethod.toBuilder();

        // lotto 관련 설정 (payback 및 piecestock과 별도로 처리)
        if ("lotto".equals(usageType)) {
            updatedPointUsageMethod.isLotto(isActive);
        }

        // payback과 piecestock에 대한 충돌 체크 후 설정
        if ("payback".equals(usageType)) {
            // 현재 상태 확인
            if (isActive && updatedPointUsageMethod.build().getIsPieceStock()) {
                throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
            }
            updatedPointUsageMethod.isPayback(isActive);
        }

        if ("piecestock".equals(usageType)) {
            // 현재 상태 확인
            if (isActive && updatedPointUsageMethod.build().getIsPayback()) {
                throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
            }
            updatedPointUsageMethod.isPieceStock(isActive);
        }

        // 활성화된 옵션 수 체크
        PointUsageMethod updatedPointUsage = updatedPointUsageMethod.build();
        int activeCount = (updatedPointUsage.getIsLotto() ? 1 : 0) +
                (updatedPointUsage.getIsPayback() ? 1 : 0) +
                (updatedPointUsage.getIsPieceStock() ? 1 : 0);

        if (activeCount < 1) {
            throw new AppException(ErrorCode.MINIMUM_ONE_OPTION_REQUIRED);
        }
        if (activeCount > 2) {
            throw new AppException(ErrorCode.MAXIMUM_TWO_OPTIONS_ALLOWED);
        }

        // 포인트 사용 방법 저장
        return repository.save(updatedPointUsage);
    }



}
