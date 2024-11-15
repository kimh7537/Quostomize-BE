package com.quostomize.quostomize_be.domain.customizer.point.service;

import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.point.repository.PointUsageMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointUsageMethodService {

    @Autowired
    private PointUsageMethodRepository pointUsageMethodRepository;

    @Autowired
    private CardRepository cardRepository;

    public PointUsageMethod CreatePointUsageMethod(Long cardId,Boolean stock,Boolean lotto,Boolean payback){
        //포인트사용방법 객체 생성
        PointUsageMethod pointUsageMethod;
        //생성된 카드 아이디를 이용해, 카드 정보 객체 생성
        CardDetail cardDetail = cardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException("카드아이디가 유효하지 않습니다."));
        if(payback == true && stock == true){ // 조각투자와 페이백이 동시에 진행 시 예외처리 진행
            throw new EntityNotFoundException("페이벡과 주식투자가 동시에 체크되어 있습니다.");
        } else{ // 예외처리 확인 후, 카드 정보를 참조하는 포인트 사용방법 객체 생성
            pointUsageMethod = new PointUsageMethod(lotto,payback,stock, cardDetail);
        }
        // 포인트사용방법 저장
        pointUsageMethodRepository.save(pointUsageMethod);
        // 포인트사용방법 객체 전달
        return pointUsageMethod;
    }
}
