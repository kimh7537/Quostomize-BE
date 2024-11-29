package com.quostomize.quostomize_be.api.sms.controller;

import com.quostomize.quostomize_be.api.sms.dto.SmsRequest;
import com.quostomize.quostomize_be.common.sms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/sms")
public class SmsCertificationController {

    private final SmsService smsService;

    @PostMapping("/send")
    @Operation(summary = "SMS 인증번호 발송", description = "사용자 휴대폰 번호로 6자리 인증번호를 발송합니다.")
    public ResponseEntity<Void> sendSms(@Valid @RequestBody SmsRequest smsRequest) {
        smsService.sendSms(smsRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirm")
    @Operation(summary = "SMS 인증번호 확인", description = "발송된 SMS 인증번호를 검증합니다.")
    public ResponseEntity<Void> verifySms(@Valid @RequestBody SmsRequest smsRequest) {
        smsService.verifySms(smsRequest);
        return ResponseEntity.noContent().build();
    }
}