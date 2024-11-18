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

        PointUsageMethod updatedPointUsageMethod = PointUsageMethod.builder()
                .pointUsageTypeId(pointUsageMethod.getPointUsageTypeId())
                .cardDetail(pointUsageMethod.getCardDetail())
                .isLotto(pointUsageMethod.getIsLotto())
                .isPayback(pointUsageMethod.getIsPayback())
                .isPieceStock(pointUsageMethod.getIsPieceStock())
                .build();

        // 사용 유형에 따른 필드 변경
        switch (usageType) {
            case "lotto":
                updatedPointUsageMethod = updatedPointUsageMethod.toBuilder()
                        .isLotto(isActive)
                        .build();
                break;

            case "payback":
                if (isActive && updatedPointUsageMethod.getIsPieceStock()) {
                    throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
                }
                updatedPointUsageMethod = updatedPointUsageMethod.toBuilder()
                        .isPayback(isActive)
                        .build();
                break;

            case "piecestock":
                if (isActive && updatedPointUsageMethod.getIsPayback()) {
                    throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
                }
                updatedPointUsageMethod = updatedPointUsageMethod.toBuilder()
                        .isPieceStock(isActive)
                        .build();
                break;
        }

        // 활성화된 옵션 수 체크
        int activeCount = (updatedPointUsageMethod.getIsLotto() ? 1 : 0) +
                (updatedPointUsageMethod.getIsPayback() ? 1 : 0) +
                (updatedPointUsageMethod.getIsPieceStock() ? 1 : 0);

        if (activeCount < 1) {
            throw new AppException(ErrorCode.MINIMUM_ONE_OPTION_REQUIRED);
        }
        if (activeCount > 2) {
            throw new AppException(ErrorCode.MAXIMUM_TWO_OPTIONS_ALLOWED);
        }

        // 포인트 사용 방법 저장
        return repository.save(updatedPointUsageMethod);
    }

}
