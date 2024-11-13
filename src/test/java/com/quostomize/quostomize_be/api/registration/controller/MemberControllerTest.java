package com.quostomize.quostomize_be.api.registration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.domain.customizer.registration.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("정보 다 잘 넣었을 때, 회원 가입을 시켜주는 자.")
    public void saveTest() throws Exception {
        // Given
        MemberRequestDto memberRequestDto = new MemberRequestDto(
                "John Doe", "john@example.com", "admin123",
                "", "bv123456", "bv123456",
                "1234567891111", "birth", "gender",
                "nickname", "12345678910", "123456", "123456"
        );

        // When
        doNothing().when(memberService).saveMember(any(MemberRequestDto.class));

        // Then
        mockMvc.perform(post("/api/member/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("정보 없이 날렸을 때, 아니라고 하는 자.")
    public void registerMember_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        MemberRequestDto invalidMemberRequestDto = new MemberRequestDto(
                "", "", "", "", "", "",
                "", "", "", "", "", "", ""
        );

        // Then
        mockMvc.perform(post("/api/member/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMemberRequestDto)))
                .andExpect(status().isBadRequest());
    }
}

