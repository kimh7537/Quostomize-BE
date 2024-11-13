package com.quostomize.quostomize_be.common.sms.service;

import com.quostomize.quostomize_be.api.sms.dto.UserDto;
import com.quostomize.quostomize_be.common.error.exception.InvalidPhoneNumberException;
import com.quostomize.quostomize_be.common.error.exception.SmsCertificationAppException;
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

    private static final int CERTIFICATION_NUMBER_LENGTH = 6;
    private static final int MIN_CERTIFICATION_NUMBER = 100_000;
    private static final int MAX_CERTIFICATION_NUMBER = 999_999;

    @Transactional
    public void sendSms(UserDto.SmsCertificationRequest requestDto) {
        validatePhoneNumber(requestDto.getPhone());

        String certificationNumber = generateCertificationNumber();
        smsUtil.sendSms(requestDto.getPhone(), certificationNumber);
        SmsCertificationRepository.createSmsCertification(requestDto.getPhone(), certificationNumber);
    }

    @Transactional
    public void verifySms(UserDto.SmsCertificationRequest requestDto) {
        if (!isCertificationValid(requestDto)) {
            throw new SmsCertificationAppException("인증번호가 일치하지 않습니다.");
        }
        SmsCertificationRepository.removeSmsCertification(requestDto.getPhone());
    }

    private String generateCertificationNumber() {
        return String.format("%06d",
                (int) (Math.random() * (MAX_CERTIFICATION_NUMBER - MIN_CERTIFICATION_NUMBER + 1)
                        + MIN_CERTIFICATION_NUMBER));
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^010\\d{8}$")) {
            throw new InvalidPhoneNumberException();
        }
    }

    private boolean isCertificationValid(UserDto.SmsCertificationRequest requestDto) {
        return SmsCertificationRepository.hasKey(requestDto.getPhone()) &&
                SmsCertificationRepository.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber());
    }
}