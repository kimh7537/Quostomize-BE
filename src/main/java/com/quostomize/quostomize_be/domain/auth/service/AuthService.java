package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.api.auth.dto.*;
import com.quostomize.quostomize_be.api.auth.dto.MemberLoginIdResponse;
import com.quostomize.quostomize_be.api.auth.dto.MemberRequestDto;
import com.quostomize.quostomize_be.api.sms.dto.SmsRequest;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.jwt.*;
import com.quostomize.quostomize_be.common.sms.service.SmsService;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    
    private static final String REFRESH_TOKEN = "refreshToken";

    private final MemberRepository memberRepository;
    private final ValidateService validateService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final EncryptService encryptService;
    private final SmsService smsService;

    @Value("${jwt.refresh.expiration}")
    private int refreshTokenAge;

    @Transactional
    public JoinResponse saveMember(MemberRequestDto request) {
        validateService.checkDuplicateLoginId(request.memberLoginId());
        validateService.checkLoginIdPattern(request.memberLoginId());
        validateService.checkDuplicateEmail(request.memberEmail());
        validateService.checkPasswordPattern(request.memberPassword());
        validateService.checkPhoneNumber(request.memberPhoneNumber());

        log.info("[회원 가입 서비스]: {}, {}", request.memberLoginId(), request.memberName());

        Member member = createMember(request);
        memberRepository.save(member);
        return JoinResponse.from(member);
    }

    private Member createMember(MemberRequestDto request) {
        String encryptedPhoneNumber = encryptService.encryptPhoneNumber(request.memberPhoneNumber());
        validateService.checkDuplicatePhoneNumber(encryptedPhoneNumber);
        String encryptedSecondaryAuthCode = encryptService.encryptSecondaryAuthCode(request.secondaryAuthCode());
        String encryptedResidenceNumber = encryptService.encryptResidenceNumber(request.residenceNumber());
        validateService.checkResidenceNumber(encryptedPhoneNumber);

        return Member.builder()
                .memberName(request.memberName())
                .memberEmail(request.memberEmail())
                .memberLoginId(request.memberLoginId())
                .memberPassword(bCryptPasswordEncoder.encode(request.memberPassword()))
                .residenceNumber(encryptedResidenceNumber)
                .zipCode(request.zipCode())
                .memberAddress(request.memberAddress())
                .memberDetailAddress(request.memberDetailAddress())
                .memberPhoneNumber(encryptedPhoneNumber)
                .secondaryAuthCode(encryptedSecondaryAuthCode)
                .build();
    }

    @Transactional
    public ReissueResponse reissue(String refreshToken, HttpServletResponse response) {
        if (jwtTokenProvider.isExpired(refreshToken) || blackListRepository.existsById(refreshToken)) {
            log.warn("블랙리스트에 존재하는 토큰: {}", blackListRepository.existsById(refreshToken));
            throw new AppException(ErrorCode.REISSUE_FAIL);
        }
        Long memberId = jwtTokenProvider.getMemberId(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        RefreshToken findToken = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_ID_NOT_FOUND));
        log.info("[브라우저에서 들어온 쿠키] == [DB에 저장된 토큰], {}", refreshToken.equals(findToken.getRefreshToken()));

        if (!refreshToken.equals(findToken.getRefreshToken())) {
            log.warn("[쿠키로 들어온 토큰과 DB의 토큰이 일치하지 않음.]");
            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_EXIST);
        }
        jwtTokenProvider.setBlackList(refreshToken);
        Token token = jwtTokenProvider.createToken(memberId, role);
        findToken.updateRefreshToken(token.getRefreshToken());
        refreshTokenRepository.save(findToken);

        Cookie refreshCookie = new Cookie(REFRESH_TOKEN, token.getRefreshToken());
        refreshCookie.setMaxAge(refreshTokenAge / 1000);
        log.info("[리프레시 쿠키 발급, 발급시간 : {}]", refreshTokenAge / 1000);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        response.addCookie(refreshCookie);
        return ReissueResponse.from(token.getAccessToken());
    }

    @Transactional
    public void logout(LogoutRequest request, String refreshToken, HttpServletResponse response) {
        Long memberId = jwtTokenProvider.getMemberId(refreshToken);
        RefreshToken existRefreshToken = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_EXIST));
        jwtTokenProvider.setBlackList(refreshToken);
        log.info("[로그아웃 된 리프레시 토큰 블랙리스트 처리]");
        refreshTokenRepository.delete(existRefreshToken);
        Cookie deleteCookie = new Cookie(REFRESH_TOKEN, null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        deleteCookie.setSecure(true);
        deleteCookie.setHttpOnly(true);
        response.addCookie(deleteCookie);
        jwtTokenProvider.setBlackList(request.accessToken());
        log.info("[로그아웃 된 액세스 토큰 블랙리스트 처리]");
    }

    public void sendFindPasswordPhoneNumber(SmsRequest request) {
        validateService.phoneNumberExist(request.phone());
        smsService.sendSms(request);
    }

    public FindPasswordResponse verifyPasswordCode(SmsRequest request) {
        smsService.verifySms(request);

        Member findMember = memberRepository.findByMemberPhoneNumber(request.phone())
                .orElseThrow(() -> new AppException(ErrorCode.PHONE_NOT_FOUND));
        String role = findMember.getRole().getKey();
        
        //비밀번호를 변경하려면 토큰을 검증해야함
        Token token = jwtTokenProvider.createToken(findMember.getMemberId(), role);
        return FindPasswordResponse.from(token.getAccessToken());
    }

    public MemberLoginIdResponse findMemberLoginId(String name, String phoneNumber) {
        String encryptedPhoneNumber = encryptService.encryptPhoneNumber(phoneNumber);
        Member findMember = memberRepository.findByMemberPhoneNumber(encryptedPhoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("해당 전화번호를 가진 회원을 찾을 수 없습니다."));
        validateMatchName(findMember.getMemberName(), name);
        return MemberLoginIdResponse.from(findMember.getMemberLoginId());
    }
    

    private void validateMatchName(String originName, String requestName) {
        if (!originName.equals(requestName)) {
            throw new EntityNotFoundException("해당 이름을 가진 회원을 찾을 수 없습니다.");
        }
    }
}
