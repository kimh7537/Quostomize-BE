//package com.quostomize.quostomize_be.api.pointUsageMethod.controller;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodRequestDto;
//import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodResponseDto;
//import com.quostomize.quostomize_be.common.dto.ResponseDTO;
//import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service.PointUsageMethodService;
//import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import org.springframework.web.context.WebApplicationContext;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class PointUsageMethodControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @MockBean
//    private PointUsageMethodService usageMethodService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .build();
//    }
//
//    @Test
//    @DisplayName("포인트 사용 옵션 조회 테스트")
//    public void test1() throws Exception {
//        // given
//        Long cardSequenceId = 1L;
//        PointUsageMethod mockUsageMethod = new PointUsageMethod();
//        PointUsageMethodResponseDto responseDto = PointUsageMethodResponseDto.from(mockUsageMethod);
//        ResponseDTO<PointUsageMethodResponseDto> expectedResponse = new ResponseDTO<>(responseDto);
//
//        Mockito.when(usageMethodService.getPointUsageMethod(cardSequenceId)).thenReturn(mockUsageMethod);
//
//        // expected
//        mockMvc.perform(get("/api/my-card/{cardSequenceId}", cardSequenceId))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
//
//        Mockito.verify(usageMethodService, Mockito.times(1)).getPointUsageMethod(cardSequenceId);
//    }
//
//    @Test
//    @DisplayName("포인트 사용 옵션 변경 테스트")
//    public void test2() throws Exception {
//
//        // given
//        Long cardSequenceId = 1L;
//        PointUsageMethodRequestDto requestDto = new PointUsageMethodRequestDto(null, null, true, true,"lotto");
//        PointUsageMethod updatedUsageMethod = new PointUsageMethod();
//        PointUsageMethodResponseDto responseDto = PointUsageMethodResponseDto.from(updatedUsageMethod);
//        ResponseDTO<PointUsageMethodResponseDto> expectedResponse = new ResponseDTO<>(responseDto);
//
//        Mockito.when(usageMethodService.togglePointUsage(cardSequenceId, requestDto.usageType(), requestDto.isActive()))
//                .thenReturn(updatedUsageMethod);
//
//        // expected
//        mockMvc.perform(post("/api/my-card/{cardSequenceId}", cardSequenceId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
//
//        Mockito.verify(usageMethodService, Mockito.times(1)).togglePointUsage(cardSequenceId, requestDto.usageType(), requestDto.isActive());
//    }
//}
