package com.quostomize.quostomize_be.domain.customizer.point.service;

import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import com.quostomize.quostomize_be.domain.customizer.point.repository.CardPointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardPointService {

    private final CardPointRepository cardPointRepository;
    private final CardDetailRepository cardDetailRepository;

    @Transactional
    public CardPoint createCardPoint(Long cardId) {
        // 카드아이디에 해당하는 카드 정보 조회
        CardDetail cardDetailById = cardDetailRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카드 신청 내역입니다."));
        // 카드 아이디에 해당하는 카드 정보와 카드 아이디를 참조하는 카드포인트 생성
        CardPoint cardPoint = new CardPoint(0L, cardDetailById);
        // 카드 포인트 정보 저장
        cardPointRepository.save(cardPoint);
        return cardPoint;
    }
}
