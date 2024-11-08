package com.quostomize.quostomize_be.api.payment.controller;

import com.quostomize.quostomize_be.api.payment.dto.PaymentRequestDto;
import com.quostomize.quostomize_be.api.payment.dto.PaymentResponseDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.response.ErrorResponse;
import com.quostomize.quostomize_be.domain.customizer.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    ResponseEntity<?> payment(@RequestBody PaymentRequestDto paymentRequestDto, HttpServletRequest request) {
        PaymentResponseDto paymentResponseDto = paymentService.payment(paymentRequestDto);
        if (paymentResponseDto == null) {
            return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.INVALID_CARD_NUMBER, request));
        }
        return ResponseEntity.created(URI.create("/api/payment"+paymentResponseDto.payment_id())).build();
    }



}
