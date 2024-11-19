package com.quostomize.quostomize_be.api.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.auth.dto.MemberRequestDto;
import com.quostomize.quostomize_be.domain.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("정보의 요구사항을 갖추고 회원가입 시도")
    public void saveTest() throws Exception {
        //Given
        MemberRequestDto memberRequestDto = new MemberRequestDto(
                "John Doe",
                "john@example.com",
                "admin123",
                "bv123456",
                "1234567891111",
                "12999",
                "1234545678912",
                "testDetailAddress",
                "01055558888",
                "123456"
        );


        doNothing().when(authService).saveMember(any(MemberRequestDto.class));

        // expected: 회원가입이 완료 되었습니다.
        mockMvc.perform(post("/v1/api/auth/join")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("정보를 입력하지 않고 회원가입 시도")
    public void returnBadRequest() throws Exception {
        // Given
        MemberRequestDto invalidMemberRequestDto = new MemberRequestDto(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        // expected: 빈 정보를 보낼 때, BadRequest 반환
        mockMvc.perform(post("/v1/api/auth/join")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMemberRequestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("잘못된 정보를 가지고 회원가입 시도")
    public void returnBadRequest2() throws Exception {
        // Given: 양식과 다른 Email, Password,PhoneNumber, SecondaryCode 및 주민번호, 주소에 관한 정보 미입력으로 총 발생할 에러 10건.
        MemberRequestDto invalidMemberRequestDto = new MemberRequestDto(
                "testName2",
                "test@example.com",
                "2312gjkdfs",
                "password123",
                "1234545678912",
                "12999",
                "1234545678912",
                "testDetailAddress",
                "01055558888",
                "123456"
        );
        // expected: 요청이 Bad Request를 반환하는지 확인
        mockMvc.perform(post("/v1/api/auth/join")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMemberRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(10));
    }
}
