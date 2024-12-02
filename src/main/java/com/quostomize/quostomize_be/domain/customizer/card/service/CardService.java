package com.quostomize.quostomize_be.domain.customizer.card.service;

import com.quostomize.quostomize_be.api.card.dto.CreateCardDTO;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CardService {

    private final CardDetailRepository cardDetailRepository;
    private final Random random = new Random();

    @Transactional
    public CardDetail createCard(CreateCardDTO createCardDTO) {
        String cardNumber = String.valueOf(random.nextLong(1_000_0000_0000_0000L,10_000_0000_0000_0000L));
        String cvc = String.valueOf(random.nextInt(100, 1000));

        // cardPassword와 cardPasswordConfirm의 값이 같은 지 검증
        if (!createCardDTO.cardPassword().equals(createCardDTO.cardPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

//        String encodedPassword = bCryptPasswordEncoder.encode(createCardDTO.cardPassword());
//        String encodedCvc = bCryptPasswordEncoder.encode(cvc);
        int expiredYear = LocalDate.now().plusYears(5).getYear();
        int expiredMonth = LocalDate.now().getMonthValue();
        LocalDate expiredAt = LocalDate.of(expiredYear,expiredMonth, 1);

        CardDetail cardDetail = CardDetail.builder()
                .cardNumber(cardNumber)
                .cardColor(createCardDTO.cardColor())
                .cardBrand(createCardDTO.cardBrand())
                .isAppCard(createCardDTO.isAppCard())
                .isForeignBlocked(createCardDTO.isForeignBlocked())
                .isPostpaidTransport(createCardDTO.isPostpaidTransport())
//                .cardPassword(encodedPassword)
//                .cvcNumber(encodedCvc)
                .cardPassword(createCardDTO.cardPassword())
                .cvcNumber(cvc)
                .expirationDate(expiredAt)
                .optionalTerms(createCardDTO.optionalTerms())
                .paymentReceiptMethods(createCardDTO.paymentReceiptMethods())
                .build();

        return cardDetailRepository.save(cardDetail);
    }
}
