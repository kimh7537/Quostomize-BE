package com.quostomize.quostomize_be.common.sms.service;

import com.quostomize.quostomize_be.api.sms.dto.SmsRequest;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.sms.repository.SmsCertificationRepository;
import com.quostomize.quostomize_be.common.sms.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsService {

    private final SmsCertificationUtil smsUtil;
    private final SmsCertificationRepository SmsCertificationRepository;

    private static final int MIN_CERTIFICATION_NUMBER = 100_000;
    private static final int MAX_CERTIFICATION_NUMBER = 999_999;

    @Transactional
    public void sendSms(SmsRequest smsRequest) {
        validatePhoneNumber(smsRequest.phone());

        String certificationNumber = generateCertificationNumber();
        smsUtil.sendSms(smsRequest.phone(), certificationNumber);
        SmsCertificationRepository.createSmsCertification(smsRequest.phone(), certificationNumber);
    }

    @Transactional
    public void verifySms(SmsRequest smsRequest) {
        if (!isCertificationValid(smsRequest)) {
            throw new AppException(ErrorCode.SMS_CERTIFICATION_ERROR);
        }
        SmsCertificationRepository.removeSmsCertification(smsRequest.phone());
    }

    private String generateCertificationNumber() {
        return String.format("%06d",
                (int) (Math.random() * (MAX_CERTIFICATION_NUMBER - MIN_CERTIFICATION_NUMBER + 1)
                        + MIN_CERTIFICATION_NUMBER));
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^010\\d{8}$")) {
            throw new AppException(ErrorCode.INVALID_PHONE_FORMAT);
        }
    }

    private boolean isCertificationValid(SmsRequest smsRequest) {
        return SmsCertificationRepository.hasKey(smsRequest.phone()) &&
                SmsCertificationRepository.getSmsCertification(smsRequest.phone())
                        .equals(smsRequest.certificationNumber());
    }
}