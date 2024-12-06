package com.quostomize.quostomize_be.api.pointUsageMethod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.config.MockUser;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service.PointUsageMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PointUsageMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PointUsageMethodService pointUsageMethodService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


    @Test
    @MockUser
    @DisplayName("포인트 사용 옵션 조회 테스트")
    void test1() throws Exception {
        // given
        Long memberId = 1L;
        List<PointUsageMethodResponse> mockCardDetails = List.of(
                new PointUsageMethodResponse(
                        1L,                  // pointUsageTypeId
                        true,                // isLotto
                        false,               // isPayback
                        false,               // isPieceStock
                        1L,                  // cardSequenceId
                        0,                   // cardColor
                        "카드번호1234",       // cardNumber
                        1L,                  // benefitId
                        5,                   // benefitRate
                        true,                // isActive
                        "상위카테고리",        // upperCategoryType
                        "하위카테고리",        // lowerCategoryType
                        "프랜차이즈이름"       // franchiseName
                )
        );
        ResponseDTO<List<PointUsageMethodResponse>> expectedResponse = new ResponseDTO<>(mockCardDetails);
        Mockito.when(pointUsageMethodService.getMyCardDetails(memberId)).thenReturn(mockCardDetails);
        // expected
        mockMvc.perform(get("/v1/api/my-card"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(pointUsageMethodService, Mockito.times(1)).getMyCardDetails(memberId);
    }

}
