package com.quostomize.quostomize_be.api.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.auth.dto.MemberRequestDto;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.auth.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("정보의 요구사항을 갖추고 회원가입 시도")
    void saveTest() throws Exception {
        //Given
        MemberRequestDto memberRequestDto = new MemberRequestDto(
                "John Doe",
                "john@example.com",
                "admin123",
                "bv123456@",
                "9999991111111",
                "12999",
                "강동에 살아",
                "testDetailAddress",
                "01055558888",
                "123456"
        );


        mockMvc.perform(post("/v1/api/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(1)) // memberId가 1인지 검증
                .andDo(print());

    }

    @Test
    @DisplayName("정보를 입력하지 않고 회원가입 시도")
    void returnBadRequest() throws Exception {
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMemberRequestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("잘못된 정보를 가지고 회원가입 시도")
    void returnBadRequest2() throws Exception {
        // Given: 양식과 다른 Email, Password,PhoneNumber, SecondaryCode 및 주민번호, 주소에 관한 정보 미입력으로 총 발생할 에러 10건.
        MemberRequestDto invalidMemberRequestDto = new MemberRequestDto(
                "testName2",
                "test@example.com",
                "2312gjkdfs",
                "password123",
                "9999991111111",
                "12999",
                "1234545678912",
                "testDetailAddress",
                "01055558888",
                "123456"
        );
        // expected: 요청이 Bad Request를 반환하는지 확인
        mockMvc.perform(post("/v1/api/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMemberRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 아이디입니다."))
                .andExpect(jsonPath("$.code").value("A-001"))
                .andDo(print());
    }
}
