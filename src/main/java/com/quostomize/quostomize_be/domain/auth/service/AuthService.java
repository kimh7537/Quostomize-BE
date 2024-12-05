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
import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.log.enums.LogStatus;
import com.quostomize.quostomize_be.domain.log.enums.LogType;
import com.quostomize.quostomize_be.domain.log.service.LogService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private static final String REFRESH_TOKEN = "refreshToken";

    private final MemberRepository memberRepository;
    private final CardApplicantInfoRepository cardApplicantInfoRepository;
    private final CustomerRepository customerRepository;
    private final ValidateService validateService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final EncryptService encryptService;
    private final SmsService smsService;
    private final LogService logService;

    @Value("${jwt.refresh.expiration}")
    private int refreshTokenAge;

    @Transactional
    public Boolean checkMemberId(String memberId){
        Optional<Member> member = memberRepository.findByMemberLoginId(memberId);
        return member.isEmpty();
    }

    @Transactional
    public JoinResponse saveMember(MemberRequestDto request) {
        validateService.checkDuplicateLoginId(request.memberLoginId());
        validateService.checkLoginIdPattern(request.memberLoginId());
        validateService.checkDuplicateEmail(request.memberEmail());
        validateService.checkPasswordPattern(request.memberPassword());
        validateService.checkPhoneNumber(request.memberPhoneNumber());

        log.info("[회원 가입 서비스]: {}, {}", request.memberLoginId(), request.memberName());

        Member member = createMember(request);
        Member saved = memberRepository.save(member);
        createCustomerIfCardApplicantExists(request.residenceNumber(), saved); //회원 가입하고 카드 신청 이력이 있으면 customer 테이블에 값을 넣어줌
        return JoinResponse.from(member);
    }

    private void createCustomerIfCardApplicantExists(String residenceNumber, Member savedMember) {
        cardApplicantInfoRepository.findByResidenceNumber(residenceNumber).ifPresent(cardApplicantInfo -> {
            Customer customer = Customer.builder()
                    .cardDetail(cardApplicantInfo.getCardDetail())
                    .member(savedMember)
                    .build();
            customerRepository.save(customer);
        });
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

        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
        }

        try {
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

            // 로그아웃 성공 로그 저장
            logService.saveLog(LogType.LOGOUT, "회원 ID: " + memberId + " 로그아웃 성공", memberId, LogStatus.SUCCESS, "/v1/api/auth/logout");

        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생: {}", e.getMessage());
            logService.saveLog(LogType.LOGOUT, "회원 ID: " + memberId + " 로그아웃 실패: " + e.getMessage(), memberId, LogStatus.ERROR, "/v1/api/auth/logout");
            throw e;
        } finally {
            MDC.clear();
        }
    }

    public void sendFindPasswordPhoneNumber(SmsRequest request) {
        String encryptedPhoneNumber = encryptService.encryptPhoneNumber(request.phone());
        validateService.phoneNumberExist(encryptedPhoneNumber);
        smsService.sendSms(request);
    }

    public FindPasswordResponse verifyPasswordCode(SmsRequest request) {
        smsService.verifySms(request);
        String encryptedPhoneNumber = encryptService.encryptPhoneNumber(request.phone());

        Member findMember = memberRepository.findByMemberPhoneNumber(encryptedPhoneNumber)
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
