package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.component.MemberReader;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberReader memberReader;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EncryptService encryptService;
    private final ValidateService validateService;

    @Transactional
    public void updatePassword(final Long memberId, final String password) {
        Member findMember = memberReader.findById(memberId);

        validateService.checkPasswordPattern(password); //패스워드 규칙에 맞는지 확인
        validateIsSameBefore(findMember.getMemberPassword(), password); //패스워드를 암호화 한 후 비교

        findMember.updatePassword(bCryptPasswordEncoder.encode(password));
    }

    private void validateIsSameBefore(String originPassword, String newPassword) {
        if (bCryptPasswordEncoder.matches(newPassword, originPassword)) {
            throw new AppException(ErrorCode.SAME_PASSWORD);
        }
    }

    @Transactional
    public void updatePhoneNumber(Long memberId, String phoneNumber) {
        Member findMember = memberReader.findById(memberId);
        String encryptedPhoneNumber = encryptService.encryptPhoneNumber(phoneNumber);
        findMember.updatePhoneNumber(encryptedPhoneNumber);
    }

}