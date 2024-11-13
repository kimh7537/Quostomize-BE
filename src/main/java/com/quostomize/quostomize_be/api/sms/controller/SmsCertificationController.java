package com.quostomize.quostomize_be.api.sms.controller;

import com.quostomize.quostomize_be.api.sms.dto.UserDto;
import com.quostomize.quostomize_be.common.sms.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card/validation-member")
public class SmsCertificationController {

    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendSms(@Valid @RequestBody UserDto.SmsCertificationRequest requestDto) {
        smsService.sendSms(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> verifySms(@Valid @RequestBody UserDto.SmsCertificationRequest requestDto) {
        smsService.verifySms(requestDto);
        return ResponseEntity.ok().build();
    }
}