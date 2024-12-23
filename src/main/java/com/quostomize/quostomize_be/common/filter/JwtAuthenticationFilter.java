package com.quostomize.quostomize_be.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.auth.dto.LoginResponse;
import com.quostomize.quostomize_be.common.auth.PrincipalDetails;
import com.quostomize.quostomize_be.common.error.exception.FilterAuthenticationException;
import com.quostomize.quostomize_be.common.jwt.JwtTokenProvider;
import com.quostomize.quostomize_be.common.jwt.RefreshToken;
import com.quostomize.quostomize_be.common.jwt.RefreshTokenRepository;
import com.quostomize.quostomize_be.common.jwt.Token;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.log.service.LogService;
import com.quostomize.quostomize_be.domain.log.enums.LogStatus;
import com.quostomize.quostomize_be.domain.log.enums.LogType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final int REFRESH_TOKEN_AGE = 259200;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerRepository customerRepository;
    private final LogService logService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.info("[login 요청]");
        ObjectMapper mapper = new ObjectMapper();

        // traceId 생성 및 MDC 설정
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        try {
            Member member = mapper.readValue(request.getInputStream(), Member.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    member.getMemberLoginId(), member.getMemberPassword());

            // MDC에 추가 정보 설정
            MDC.put("userId", member.getMemberLoginId());
            MDC.put("requestUri", request.getRequestURI());

            // 로그인 시도 로그 저장
            logService.saveLog(LogType.LOGIN_ATTEMPT, "ID: " + member.getMemberLoginId() + " 사용자가 로그인 시도를 했습니다.", null, LogStatus.INFO, request.getRequestURI());

            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            logService.saveLog(LogType.LOGIN_FAILURE, "로그인 실패: " + e.getMessage(), null, LogStatus.FAILURE, request.getRequestURI());
            throw new FilterAuthenticationException("로그인 시도에 실패했습니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        String grantedAuthority = authResult.getAuthorities().stream()
                .findAny()
                .orElseThrow()
                .toString();

        Token token = jwtTokenProvider.createToken(principal.getMember().getMemberId(), grantedAuthority);

        String accessToken = token.getAccessToken();
        response.addHeader("accessToken", accessToken);

        long memberId = principal.getMember().getMemberId();
        String memberName = principal.getMember().getMemberName();

        RefreshToken refreshToken = new RefreshToken(principal.getMember().getMemberId(), token.getRefreshToken());
        refreshToken.updateRefreshToken(token.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
        cookie.setPath("/");
        ZonedDateTime seoulTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime expirationTime = seoulTime.plusSeconds(REFRESH_TOKEN_AGE);
        cookie.setMaxAge((int) (expirationTime.toEpochSecond() - seoulTime.toEpochSecond()));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        String traceId = MDC.get("traceId");
        // MDC 설정 및 로그 기록
        MDC.put("userId", String.valueOf(memberId));
        MDC.put("requestUri", request.getRequestURI());

        // 응답 데이터 작성
        LoginResponse loginResponse = new LoginResponse("로그인 성공", grantedAuthority, findCardStatus(principal), memberId, memberName, traceId);
        writeJsonResponse(response, loginResponse);

        log.info("로그인 성공, JWT 토큰 생성");
        logService.saveLog(LogType.LOGIN_SUCCESS, memberName + "(ID:" + memberId + ")" + " 사용자가 로그인 했습니다.", memberId, LogStatus.SUCCESS, request.getRequestURI());
    }

    private String findCardStatus(PrincipalDetails principal) {
        Customer customer = customerRepository.findWithCardDetailByMember(principal.getMember()).orElse(null);
        return (customer != null && customer.getCardDetail() != null)
                ? customer.getCardDetail().getStatus().getKey()
                : null;
    }

    private void writeJsonResponse(HttpServletResponse response, Object responseObject) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseObject));
    }
}
