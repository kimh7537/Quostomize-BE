package com.quostomize.quostomize_be.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.config.MockUser;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import com.quostomize.quostomize_be.domain.customizer.point.repository.CardPointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@SpringBootTest
@AutoConfigureMockMvc
class CardPointControllerTest {

    @Autowired
    private CardDetailRepository cardDetailRepository;

    @Autowired
    private CardPointRepository cardPointRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clean(){
        cardDetailRepository.deleteAll();
        cardPointRepository.deleteAll();
    }

    @Test
    @DisplayName("카드 생성 시, 카드 포인트를 생성한다.")
    public void createCardPoint() throws Exception {
        //given - 카드 데이터를 미리 데이터베이스에 저장
        CardDetail cardDetail = CardDetail.builder()
                .paymentReceiptMethods(1)
                .optionalTerms(1)
                .expirationDate(LocalDate.now().plusYears(3))
                .cvcNumber("123")
                .cardPassword("1234")
                .isPostpaidTransport(true)
                .isForeignBlocked(false)
                .isAppCard(true)
                .cardBrand(1)
                .cardColor(1)
                .cardNumber("1234567890123456")
                .cardSequenceId(32L)
                .build();
        cardDetailRepository.save(cardDetail);

        CardPoint cardPoint = new CardPoint(0L,cardDetail);
        String json = objectMapper.writeValueAsString(cardPoint);

        //then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/point")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(json)
                                                    .param("cardId","32"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}