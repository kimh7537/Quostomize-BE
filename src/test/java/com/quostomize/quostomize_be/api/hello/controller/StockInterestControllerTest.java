package com.quostomize.quostomize_be.api.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.hello.dto.StockInterestDto;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application.yml")
@SpringBootTest
@AutoConfigureMockMvc
class StockInterestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("위시리스트 조회")
    @Disabled
    void getStockWishList() throws Exception {
        // given
        List<StockInterestDto> expectedDto = new ArrayList<>();
        expectedDto.add(new StockInterestDto(1, "LG전자", 120000, ""));
        expectedDto.add(new StockInterestDto(2, "현대차", 180000, ""));
        String json = objectMapper.writeValueAsString(expectedDto);

        //then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/stocks/select").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}