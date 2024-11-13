package com.quostomize.quostomize_be.domain.customizer.point.service;

import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import com.quostomize.quostomize_be.domain.customizer.point.repository.CardPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardPointService {

    @Autowired
    private final CardPointRepository cardPointRepository;

    @Autowired
    private final CardRepository cardRepository;

    public void createCardPoint(Long card_id) {
        // 카드포인트 객체 생성
        CardPoint cardPoint = new CardPoint();
        cardPoint.setCardPoint(0L);
        // 외래키로 참조할 cardDetail값 찾기
        CardDetail cardDetail = cardRepository.findById(card_id)
                .orElseThrow(() -> new RuntimeException("CardDetail not found"));
        // 카드포인트에 외래키 설정
        cardPoint.setCardDetail(cardDetail);
        // 카드포인트 저장
        cardPointRepository.save(cardPoint);
    }
}
