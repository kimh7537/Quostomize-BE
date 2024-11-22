package com.quostomize.quostomize_be.common.sms.service;

import com.quostomize.quostomize_be.api.sms.dto.SmsRequest;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.sms.repository.SmsCertificationRepository;
import com.quostomize.quostomize_be.common.sms.util.SmsCertificationUtil;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    @Mock
    private SmsCertificationUtil smsUtil;

    @Mock
    private SmsCertificationRepository smsCertificationRepository;

    @Test
    @DisplayName("유효한 전화번호로 SMS 인증번호 발송 성공")
    void sendSms_ValidPhoneNumber() {
        // given
        SmsRequest request = new SmsRequest("01012345678", null);

        // 모의 객체 동작 정의
        SingleMessageSentResponse mockResponse = mock(SingleMessageSentResponse.class);
        when(smsUtil.sendSms(anyString(), anyString())).thenReturn(mockResponse);

        // when
        smsService.sendSms(request);

        // then
        verify(smsUtil, times(1)).sendSms(anyString(), anyString());
        verify(smsCertificationRepository, times(1)).createSmsCertification(anyString(), anyString());
    }

    @Test
    @DisplayName("유효하지 않은 전화번호로 SMS 인증번호 발송 시 예외 발생")
    void sendSms_InvalidPhoneNumber_ThrowsException() {
        // given
        SmsRequest request = new SmsRequest("invalid", null);

        // when, then
        assertThrows(AppException.class, () -> smsService.sendSms(request));
    }

    @Test
    @DisplayName("유효한 인증번호로 SMS 인증 성공")
    void verifySms_ValidCertification_Success() {
        // given
        SmsRequest request = new SmsRequest("01012345678", "123456");

        when(smsCertificationRepository.hasKey(anyString())).thenReturn(true);
        when(smsCertificationRepository.getSmsCertification(anyString())).thenReturn("123456");

        // when
        smsService.verifySms(request);

        // then
        verify(smsCertificationRepository, times(1)).removeSmsCertification(anyString());
    }

    @Test
    @DisplayName("인증번호 불일치로 SMS 인증 실패")
    void verifySms_InvalidCertification_ThrowsException() {
        // given
        SmsRequest request = new SmsRequest("01012345678", "123456");

        when(smsCertificationRepository.hasKey(anyString())).thenReturn(true);
        when(smsCertificationRepository.getSmsCertification(anyString())).thenReturn("654321");

        // when, then
        assertThrows(AppException.class, () -> smsService.verifySms(request));
    }

    @Test
    @DisplayName("SMS 인증번호 만료 시 예외 발생")
    void verifySms_ExpiredCertification_ThrowsException() {
        // given
        SmsRequest request = new SmsRequest("01012345678", "123456");

        // 인증번호가 존재하지만 만료된 상황
        when(smsCertificationRepository.hasKey(anyString())).thenReturn(false);

        // when, then
        assertThrows(AppException.class, () -> smsService.verifySms(request));
    }
}