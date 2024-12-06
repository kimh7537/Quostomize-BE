package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.api.auth.dto.LogoutRequest;
import com.quostomize.quostomize_be.api.auth.dto.MemberRequestDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.jwt.JwtTokenProvider;
import com.quostomize.quostomize_be.common.jwt.RefreshToken;
import com.quostomize.quostomize_be.common.jwt.RefreshTokenRepository;
import com.quostomize.quostomize_be.common.sms.service.SmsService;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.log.enums.LogStatus;
import com.quostomize.quostomize_be.domain.log.enums.LogType;
import com.quostomize.quostomize_be.domain.log.service.LogService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    private ValidateService validateService;

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

    @Mock
    private EncryptService encryptService;

    @Mock
    private LogService logService;


    @Test
    @DisplayName("중복된 이메일로 인한 예외 발생 테스트")
    void saveMember_DuplicateEmail_Exception() {
        // given
        MemberRequestDto request = new MemberRequestDto(
                "testName",
                "test@example.com",
                "testLoginId",
                "password123",
                "1234567891011",
                "12345",
                "testAddress",
                "testAddress111",
                "01012345645",
                "456789"
        );

        doThrow(new AppException(ErrorCode.EMAIL_DUPLICATED)).when(validateService).checkDuplicateEmail(request.memberEmail());

        // when & then
        AppException exception = assertThrows(AppException.class, () -> authService.saveMember(request));
        assertEquals(ErrorCode.EMAIL_DUPLICATED, exception.getErrorCode());
    }


    @Test
    @DisplayName("유효하지 않은 아이디로 회원가입 시도")
    void notValid_LoginId_Exception() {
        // given
        MemberRequestDto notValidLoginId = new MemberRequestDto(
                "testName2",
                "test@example.com",
                "2312gjkdfs",
                "password123",
                "1234545678912",
                "12999",
                "testAddress",
                "testDetailAddress",
                "01055558888",
                "123456"
        );

        doThrow(new AppException(ErrorCode.INVALID_LOGIN_ID)).when(validateService).checkLoginIdPattern(notValidLoginId.memberLoginId());

        // when & then
        AppException exception = assertThrows(AppException.class, () -> authService.saveMember(notValidLoginId));
        assertEquals(ErrorCode.INVALID_LOGIN_ID, exception.getErrorCode());
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
        verify(logService, times(1)).saveLog(LogType.LOGOUT, "회원 ID: " + memberId + " 로그아웃 성공", memberId, LogStatus.SUCCESS, "/v1/api/auth/logout");
    }

}