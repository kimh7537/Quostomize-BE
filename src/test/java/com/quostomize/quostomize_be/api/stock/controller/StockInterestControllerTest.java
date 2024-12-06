//package com.quostomize.quostomize_be.api.stock.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.quostomize.quostomize_be.api.stock.dto.StockInterestDto;
//import com.quostomize.quostomize_be.api.stock.dto.StockRecommendResponse;
//import com.quostomize.quostomize_be.config.MockUser;
//import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
//import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInterestRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class StockInterestControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private StockInterestRepository stockInterestRepository;
//
//    @AfterEach
//    void tearDown() {
//        stockInterestRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("위시리스트 조회")
//    @MockUser
//    void getStockWishList() throws Exception {
//        // given
//        LocalDate now = LocalDate.now();
//        CardDetail expectedCardDetail = CardDetail.builder()
//                .cardNumber("9876987612341234")
//                .cardColor(1)
//                .cardBrand(0)
//                .isAppCard(true)
//                .isForeignBlocked(false)
//                .isPostpaidTransport(true)
//                .cardPassword("1234")
//                .cvcNumber("888")
//                .expirationDate(LocalDate.of(now.getYear() + 5, now.getMonthValue(), 1))
//                .optionalTerms(2)
//                .paymentReceiptMethods(0)
//                .build();
//
//        when()
//
//        List<StockInterestDto> expectedDto = new ArrayList<>();
//        expectedDto.add(new StockInterestDto(1, "LG전자", 120000, ""));
//        expectedDto.add(new StockInterestDto(2, "현대차", 180000, ""));
//        String json = objectMapper.writeValueAsString(expectedDto);
//
//        //then
//        this.mockMvc.perform(get("/v1/api/stocks/select")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("cardId","1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//    @Test
//    @DisplayName("위시삭제")
//    @MockUser
//    void deleteStock() throws Exception{
//        // given
//        List<StockInterestDto> expectedDto = new ArrayList<>();
//        expectedDto.add(new StockInterestDto(1, "LG전자", 120000, ""));
//        expectedDto.add(new StockInterestDto(2, "현대차", 180000, ""));
//        String json = objectMapper.writeValueAsString(expectedDto);
//
//        //then
//        this.mockMvc.perform(delete("/v1/api/stocks/select")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .param("order","2"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//    @Test
//    @DisplayName("위시 순위 조정")
//    @MockUser
//    void switchingStock() throws Exception {
//        // given
//        List<StockInterestDto> expectedDto = new ArrayList<>();
//        expectedDto.add(new StockInterestDto(1, "LG전자", 120000, ""));
//        expectedDto.add(new StockInterestDto(2, "현대차", 180000, ""));
//        String json = objectMapper.writeValueAsString(expectedDto);
//
//        //then
//        this.mockMvc.perform(patch("/v1/api/stocks/select/change-rank")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .param("currentOrder", "2")
//                        .param("editOrder", "1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//    @Test
//    @DisplayName("종목 추천")
//    @MockUser
//    void getRecommendStocks() throws Exception {
//        List<StockRecommendResponse> expectedDto = new ArrayList<>();
//        expectedDto.add(new StockRecommendResponse("밀리의 서재",130000,"image/quokka.png"));
//        expectedDto.add(new StockRecommendResponse("현대 홈쇼핑",130000,"image/quokka.png"));
//        expectedDto.add(new StockRecommendResponse("sk 주유소",130000,"image/quokka.png"));
//        String json = objectMapper.writeValueAsString(expectedDto);
//
//        //then
//        this.mockMvc.perform(get("/v1/api/stocks/recommendations")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .param("cardId","10")
//                        .param("isRecommendByCardBenefit","true"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
//}