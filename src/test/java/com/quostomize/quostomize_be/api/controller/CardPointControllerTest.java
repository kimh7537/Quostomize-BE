package com.quostomize.quostomize_be.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application.yml")
@SpringBootTest
@AutoConfigureMockMvc
class CardPointControllerTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("카드 생성 시, 카드 포인트를 생성한다.")
    public void createCardPoint() throws Exception {
        //given
        CardDetail cardDetailById = cardRepository.findById(32L)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카드 신청 내역입니다."));
        CardPoint cardPoint = new CardPoint(0L,cardDetailById);
        String json = objectMapper.writeValueAsString(cardPoint);

        //then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/point").contentType(MediaType.APPLICATION_JSON).content(json).param("cardId","32"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}