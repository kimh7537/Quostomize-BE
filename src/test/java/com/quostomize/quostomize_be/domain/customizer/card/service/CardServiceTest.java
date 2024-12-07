package com.quostomize.quostomize_be.domain.customizer.card.service;

import com.quostomize.quostomize_be.api.card.dto.CreateCardDTO;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.auth.service.EncryptService;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardDetailRepository cardDetailRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EncryptService encryptService;

    @Mock
    private Random random;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    @DisplayName("카드 생성 성공 테스트")
//    void createCard_Success() {
//        // given
//        CreateCardDTO createCardDTO = new CreateCardDTO(
//                1,
//                0,
//                true,
//                false,
//                true,
//                "1234",
//                "1234",
//                2,
//                0
//        );
//
//        String cardNumber = "9876987612341234";
//        String cvc = "888";
//
//        LocalDate now = LocalDate.now();
//        LocalDate expiredAt = LocalDate.of(now.getYear() + 5, now.getMonthValue(), 1);
//
//        CardDetail expectedCardDetail = CardDetail.builder()
//                .cardNumber(cardNumber)
//                .cardColor(1)
//                .cardBrand(0)
//                .isAppCard(true)
//                .isForeignBlocked(false)
//                .isPostpaidTransport(true)
//                .cardPassword("1234")
//                .cvcNumber(cvc)
//                .expirationDate(expiredAt)
//                .optionalTerms(2)
//                .paymentReceiptMethods(0)
//                .status(CardStatus.CREATION_PENDING)
//                .build();
//
//        when(random.nextLong(any(Long.class), any(Long.class))).thenReturn(Long.parseLong(cardNumber));
//        when(random.nextInt(100, 1000)).thenReturn(Integer.parseInt(cvc));
//        when(cardDetailRepository.save(any(CardDetail.class))).thenReturn(expectedCardDetail);
//
//        // when
//        CardDetail createdCard = cardService.createCard(createCardDTO);
//
//        // then
//        assertThat(createdCard).isNotNull();
//        assertEquals(expectedCardDetail.getCardColor(), createdCard.getCardColor());
//        assertEquals(expectedCardDetail.getCardBrand(), createdCard.getCardBrand());
//        assertEquals(expectedCardDetail.getIsAppCard(), createdCard.getIsAppCard());
//        assertEquals(expectedCardDetail.getIsForeignBlocked(), createdCard.getIsForeignBlocked());
//        assertEquals(expectedCardDetail.getIsPostpaidTransport(), createdCard.getIsPostpaidTransport());
//        assertEquals(expectedCardDetail.getCardPassword(), createdCard.getCardPassword());
//        assertEquals(expectedCardDetail.getOptionalTerms(), createdCard.getOptionalTerms());
//        assertEquals(expectedCardDetail.getPaymentReceiptMethods(), createdCard.getPaymentReceiptMethods());
//
//        // Repository의 save 메소드가 호출되었는지 확인
//        verify(cardDetailRepository).save(any(CardDetail.class));
//    }

    @Test
    @DisplayName("비밀번호가 일치하지 않을 경우 예외 발생 테스트")
    void createCard_WithMismatchedPassword_ThrowsException() {
        // given
        CreateCardDTO createCardDTO = new CreateCardDTO(
                0,
                1,
                true,
                false,
                true,
                "1234",
                "5678", // 불일치하는 비밀번호
                2,
                1
        );

        // when & then
        assertThatThrownBy(() -> cardService.createCard(createCardDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
