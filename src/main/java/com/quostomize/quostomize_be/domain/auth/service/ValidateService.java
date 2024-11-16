package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateService {

    /**
     * 비밀번호 검증을 위한 정규표현식:
     *
     * 조건:
     * - 비밀번호는 8자 이상, 16자 이하여야 합니다.
     * - 적어도 하나의 영어 알파벳 (대소문자 무관)이 포함되어야 합니다.
     * - 적어도 하나의 숫자 (0-9)가 포함되어야 합니다.
     * - 적어도 하나의 특수 문자 (@, $, !, %, *, #, ?, &, . 중 하나)가 포함되어야 합니다.
     * - 허용된 문자 외에는 사용할 수 없습니다 (공백 및 다른 특수 문자 불가).
     *
     * 정규표현식 구성:
     * - ^: 문자열의 시작
     * - (?=.*[A-Za-z]): 적어도 하나의 영어 알파벳 포함
     * - (?=.*\\d): 적어도 하나의 숫자 포함
     * - (?=.*[@$!%*#?&.]): 적어도 하나의 지정된 특수 문자 포함
     * - [A-Za-z\\d@$!%*#?&.]{8,16}: 허용된 문자 집합으로 최소 8자, 최대 16자 구성
     * - $: 문자열의 끝
     */
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&.])[A-Za-z\\d@$!%*#?&.]{8,16}$";
    private static final int PHONE_NUMBER_LENGTH = 11;
    private static final String PHONE_NUMBER_PREFIX = "010";
    private final MemberRepository memberRepository;

    public void checkDuplicateLoginId(String loginId){
        if(memberRepository.findByMemberLoginId(loginId).isPresent()){
            log.error("[회원 가입 실패]: 중복된 아이디 " + loginId);
            throw new AppException(ErrorCode.LOGIN_ID_DUPLICATED);
        }
    }


    public void checkDuplicateEmail(String email) {
        if (memberRepository.findByMemberEmail(email).isPresent()) {
            log.error("[회원 가입 실패]: 중복된 이메일 " + email);
            throw new AppException(ErrorCode.EMAIL_DUPLICATED);
        }
    }

    public void checkDuplicatePhoneNumber(String phone) {
        if (memberRepository.findByMemberPhoneNumber(phone).isPresent()) {
            log.error("[회원 가입 실패]: 존재하는 전화번호 " + phone);
            throw new AppException(ErrorCode.PHONE_NUMBER_DUPLICATED);
        }
    }

    public void loginIdExist(String email) {
        if (!memberRepository.existsByMemberLoginId(email)) {
            throw new AppException(ErrorCode.LOGIN_ID_NOT_FOUND);
        }
    }

    public void loginIdNotExist(String email) {
        if (memberRepository.existsByMemberLoginId(email)) {
            throw new AppException(ErrorCode.LOGIN_ID_DUPLICATED);
        }
    }

    public void phoneNumberExist(String phoneNumber) {
        if (!memberRepository.existsByMemberPhoneNumber(phoneNumber)) {
            throw new AppException(ErrorCode.PHONE_NOT_FOUND);
        }
    }

    public void phoneNumberNotExist(String phoneNumber) {
        if (memberRepository.existsByMemberPhoneNumber(phoneNumber)) {
            throw new AppException(ErrorCode.PHONE_DUPLICATED);
        }
    }

    public void emailExist(String email) {
        if (!memberRepository.existsByMemberEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_NOT_FOUND);
        }
    }

    public void emailNotExist(String email) {
        if (memberRepository.existsByMemberEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_DUPLICATED);
        }
    }

    public void checkPasswordPattern(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public void checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != PHONE_NUMBER_LENGTH) {
            log.error("[전화번호 에러]: 적당하지 않은 길이");
            throw new AppException(ErrorCode.INVALID_PHONE_NUMBER);
        }
        if (!phoneNumber.startsWith(PHONE_NUMBER_PREFIX)) {
            log.error("[전화번호 에러]: 010으로 시작하지 않음");
            throw new AppException(ErrorCode.INVALID_PHONE_NUMBER);
        }
        try {
            Integer.parseInt(phoneNumber);
        } catch (Exception e) {
            log.error("[전화번호 에러]: 문자열 파싱 에러");
            throw new AppException(ErrorCode.INVALID_PHONE_NUMBER);
        }
    }
}
