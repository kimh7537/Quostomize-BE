package com.quostomize.quostomize_be.api.auth.controller;

import com.quostomize.quostomize_be.api.auth.dto.*;
import com.quostomize.quostomize_be.api.auth.dto.MemberLoginIdResponse;
import com.quostomize.quostomize_be.api.auth.dto.MemberRequestDto;
import com.quostomize.quostomize_be.api.member.dto.UpdatePasswordRequest;
import com.quostomize.quostomize_be.api.sms.dto.SmsRequest;
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

    @GetMapping("/checkId")
    @Operation(summary = "아이디 중복 여부 조회", description = "회원가입 시, 아이디 중복 여부를 확인 합니다.")
    public ResponseEntity<Boolean> checkMemberId(@RequestParam String memberId) {
        return ResponseEntity.ok(authService.checkMemberId(memberId));
    }

    @PostMapping("/join")
    @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다.")
    public ResponseEntity<JoinResponse> joinAuth(@RequestBody @Valid MemberRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.saveMember(request));
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "액세스 토큰 만료시 리프레시 토큰을 사용해 재발급합니다.")
    public ResponseEntity<ReissueResponse> tokenReissue(@CookieValue(name = "refreshToken") String refreshToken,
                                                        HttpServletResponse response) {
        log.info("[액세스 토큰 재발급 컨트롤러]: 쿠키 존재 여부, {}", !refreshToken.isEmpty());
        return ResponseEntity.ok(authService.reissue(refreshToken, response));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken") String refreshToken,
                                       @RequestBody @Valid LogoutRequest request, HttpServletResponse response) {
        authService.logout(request, refreshToken, response);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping(value = "/search-password/send")
    @Operation(summary = "비밀번호 찾기 - 전화번호로 코드 요청", description = "로그인을 진행하기 전에 비밀번호를 찾기 위해서 핸드폰 번호를 입력하고 검증을 진행합니다")
    public ResponseEntity<Void> sendFindPasswordVerificationCode(@RequestBody @Valid SmsRequest request) {
        authService.sendFindPasswordPhoneNumber(request);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/search-password/confirm")
    @Operation(summary = "비밀번호 찾기 - 받은 코드를 검증", description = "전화번호로 발송된 코드를 입력하고, 올바른 코드라면 비밀번호 변경을 위한 액세스토큰 발급")
    public ResponseEntity<FindPasswordResponse> verifyPasswordCode(@RequestBody @Valid SmsRequest request) {
        return ResponseEntity.ok(authService.verifyPasswordCode(request));
    }


    @GetMapping("/search-id")
    @Operation(summary = "아이디 찾기", description = "로그인을 진행하기 전에 아이디를 찾습니다.")
    public ResponseEntity<MemberLoginIdResponse> findMemberLoginId(@RequestParam(name = "name") String name,
                                                           @RequestParam("phone") String phoneNumber) {
        log.info("아이디 찾기 컨트롤러: {}", name);
        return ResponseEntity.ok(authService.findMemberLoginId(name, phoneNumber));
    }
}
