package com.quostomize.quostomize_be.api.registration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.domain.customizer.registration.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
    public void memberForm_ShouldReturnEmptyMemberRequestDto() throws Exception {
        mockMvc.perform(get("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(""))
                .andExpect(jsonPath("$.email").value(""))
                // Add other field checks as needed
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void registerMember_ShouldReturnSuccessMessage() throws Exception {
        // Given
        MemberRequestDto memberRequestDto = new MemberRequestDto(
                "John Doe", "john@example.com", "password",
                "address", "detail_address", "zipcode",
                "phone", "birth", "gender",
                "nickname", "marketing_agree", "terms_agree"
        );

        // When
        doNothing().when(memberService).saveMember(any(MemberRequestDto.class));

        // Then
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @Test
    public void registerMember_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        MemberRequestDto invalidMemberRequestDto = new MemberRequestDto(
                "", "", "", "", "", "",
                "", "", "", "", "", ""
        );

        // Then
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMemberRequestDto)))
                .andExpect(status().isBadRequest());
    }
}

