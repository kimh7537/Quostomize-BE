package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.api.auth.dto.LogoutRequest;
import com.quostomize.quostomize_be.api.auth.dto.MemberRequestDto;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.jwt.JwtTokenProvider;
import com.quostomize.quostomize_be.common.jwt.RefreshToken;
import com.quostomize.quostomize_be.common.jwt.RefreshTokenRepository;
import com.quostomize.quostomize_be.common.sms.service.SmsService;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CardApplicantInfoRepository cardApplicantInfoRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private SmsService smsService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
//        reset(memberRepository, cardApplicantInfoRepository, customerRepository, jwtTokenProvider, refreshTokenRepository, smsService);
        memberRepository.deleteAll();
    }

    @Mock
    private JdbcTemplate jdbcTemplate; //Customer_id 에 test가 영향을 받아 잠시 Fk 비활성화 하기 위해 사용

    @Mock
    private MemberService memberService;

//    @BeforeEach
//    void setUp(){
//        jdbcTemplate.execute("SET foreign_key_checks = 0;"); // 외래 키 제약 조건 비활성화
//    }
//
//    @AfterEach
//    void tearDown() {
//        jdbcTemplate.execute("SET foreign_key_checks = 1;"); // 외래 키 제약 조건 활성화
//    }


    @Test
    @DisplayName("중복된 이메일로 인한 예외 발생 테스트")
    void saveMember_DuplicateEmail_Exception() {
        // given
        Member member = Member.builder()
                .memberName("testName")
                .memberEmail("test@example.com")
                .memberLoginId("testLoginId")
                .memberPassword("password123")
                .residenceNumber("1234567891011")
                .zipCode("12345")
                .memberAddress("testAddress")
                .memberDetailAddress("testAddress111")
                .memberPhoneNumber("01012345645")
                .secondaryAuthCode("456789")
                .build();

        // 엔티티 저장
        memberRepository.save(member);

        // 중복된 이메일로 회원가입 시도
        MemberRequestDto duplicateEmailDto = new MemberRequestDto(
                "testName2",
                "test@example.com",
                "testLoginId2",
                "password123",
                "1234545678912",
                "12999",
                "1234545678912",
                "testDetailAddress",
                "01055558888",
                "123456"
        );
        // when & then
        AppException exception = assertThrows(AppException.class, () -> authService.saveMember(duplicateEmailDto));
        assertEquals("EMAIL_DUPLICATED", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("유효하지 않은 아이디로 회원가입 시도")
    void notValid_LoginId_Exception(){
        // given
        // 중복된 이메일로 회원가입 시도
        MemberRequestDto notValidLoginId = new MemberRequestDto(
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
        // when & then
        AppException exception = assertThrows(AppException.class, () -> authService.saveMember(notValidLoginId));
        assertEquals("INVALID_LOGIN_ID", exception.getErrorCode().name());
    }


    @Test
    @DisplayName("로그아웃 성공")
    void logout_Success() {
        // Given
        String refreshToken = "validRefreshToken";
        Long memberId = 1L;
        RefreshToken refreshTokenEntity = new RefreshToken(memberId, refreshToken);

        when(jwtTokenProvider.getMemberId(refreshToken)).thenReturn(memberId);
        when(refreshTokenRepository.findById(memberId)).thenReturn(Optional.of(refreshTokenEntity));

        HttpServletResponse response = mock(HttpServletResponse.class);

        // When
        authService.logout(new LogoutRequest("accessToken"), refreshToken, response);

        // Then
        verify(refreshTokenRepository, times(1)).delete(refreshTokenEntity);
        verify(jwtTokenProvider, times(1)).setBlackList("accessToken");
        verify(jwtTokenProvider, times(1)).setBlackList(refreshToken);
    }

}