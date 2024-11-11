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

    private final PointUsageMethodRepository repository;

    @Override
    public PointUsageMethod getPointUsageMethod(Long cardSequenceId) {

        return repository.findByCardDetail_CardSequenceId(cardSequenceId)
                .orElseThrow(()-> new AppException(ErrorCode.CARD_NOT_FOUND));
    }

    @Override
    public PointUsageMethod togglePointUsage(Long cardSequenceId, String usageType, boolean isActive) {
        PointUsageMethod pointUsageMethod = repository.findByCardDetail_CardSequenceId(cardSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));

        // 사용 유형에 따른 필드 변경
        switch (usageType) {
            case "lotto":
                pointUsageMethod.setIsLotto(isActive);
                break;
            case "payback":
                if (isActive && pointUsageMethod.getIsPieceStock()) {
                    throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
                }
                pointUsageMethod.setIsPayback(isActive);
                break;
            case "pieceStock":
                if (isActive && pointUsageMethod.getIsPayback()) {
                    throw new AppException(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
                }
                pointUsageMethod.setIsPieceStock(isActive);
                break;
        }

        return repository.save(pointUsageMethod);
    }


}
