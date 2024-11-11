package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointUsageMethodServiceImpl implements PointUsageMethodService {

    private final PointUsageMethodRepository pointUsageMethodRepository;

    @Override
    public PointUsageMethod togglePointUsage(Long cardSequenceId, String usageType, boolean isActive) {
        PointUsageMethod pointUsageMethod = pointUsageMethodRepository.findByCardSequenceId(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));

        // 사용 유형에 따른 필드 변경
        switch (usageType) {
            case "isLotto":
                pointUsageMethod.setIsLotto(isActive);
                break;
            case "isPayback":
                if (isActive && pointUsageMethod.getIsPieceStock()) {
                    throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
                }
                pointUsageMethod.setIsPayback(isActive);
                break;
            case "isPieceStock":
                if (isActive && pointUsageMethod.getIsPayback()) {
                    throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
                }
                pointUsageMethod.setIsPieceStock(isActive);
                break;
        }

        return pointUsageMethodRepository.save(pointUsageMethod);
    }

}
