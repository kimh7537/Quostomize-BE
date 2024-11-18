package com.quostomize.quostomize_be.api.auth.controller;

import com.quostomize.quostomize_be.api.auth.dto.*;
import com.quostomize.quostomize_be.api.auth.dto.MemberLoginIdResponse;
import com.quostomize.quostomize_be.domain.auth.service.AuthService;
import com.quostomize.quostomize_be.domain.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "인증 관련 API", description = "회원 인증 관련 API 모음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @Operation(summary = "회원 가입 API")
    @PostMapping("/join")
    public ResponseEntity<JoinResponse> joinAuth(@RequestBody @Valid JoinRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createLoginInfo(request));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> tokenReissue(@CookieValue(name = "refreshToken") String refreshToken,
                                                        HttpServletResponse response) {
        log.info("[액세스 토큰 재발급 컨트롤러]: 쿠키 존재 여부, {}", !refreshToken.isEmpty());
        return ResponseEntity.ok().body(authService.reissue(refreshToken, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken") String refreshToken,
                                       @RequestBody @Valid LogoutRequest request, HttpServletResponse response) {
        authService.logout(request, refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    //TODO: Service로직 완성 안함
    @PostMapping(value = "/verification", params = "type=sign-up")
    public ResponseEntity<Void> sendSignUpVerificationCode(@Valid @RequestBody SendPhoneRequest request) {
        log.info("[회원 가입 시 핸드폰 인증 요청 컨트롤러]: 요청된 이메일 {}", request.phoneNumber());
        authService.sendSignUpPhoneNumber(request);
        return ResponseEntity.noContent().build();
    }

    //TODO: Service로직 완성 안함
    @GetMapping(value = "/verification", params = "type=sign-up")
    public ResponseEntity<Void> verifyCode(@RequestParam(name = "phoneNumber") String phoneNumber,
                                           @RequestParam(name = "code") String code) {
        log.info("[회원 가입 시 인증 코드 확인 컨트롤러]: {}", phoneNumber);
        authService.verifySingUpCode(phoneNumber, code);
        return ResponseEntity.noContent().build();
    }

    //TODO: Service로직 완성 안함, 비밀번호를 찾을 때 핸드폰 번호를 인증하고 내 번호로 코드 발송
    @PostMapping(value = "/verification", params = "type=find-password")
    public ResponseEntity<Void> sendFindPasswordVerificationCode(@RequestBody @Valid SendPhoneRequest request) {
        authService.sendFindPasswordPhoneNumber(request);
        return ResponseEntity.noContent().build();
    }

    //TODO: Service로직 완성 안함
    @GetMapping(value = "/verification", params = "type=find-password")
    public ResponseEntity<FindPasswordResponse> verifyPasswordCode(@RequestParam(name = "phoneNumber") String phoneNumber,
                                                                   @RequestParam(name = "code") String code) {
        return ResponseEntity.ok().body(authService.verifyPasswordCode(phoneNumber, code));
    }


    @GetMapping("/search-id")
    public ResponseEntity<MemberLoginIdResponse> findMemberLoginId(@RequestParam(name = "name") String name,
                                                           @RequestParam("phone") String phoneNumber) {
        log.info("아이디 찾기 컨트롤러: {}", name);
        return ResponseEntity.ok(authService.findMemberLoginId(name, phoneNumber));
    }
}
