package com.quostomize.quostomize_be.api.payment.controller;

import com.quostomize.quostomize_be.api.payment.dto.PaymentRequestDto;
import com.quostomize.quostomize_be.api.payment.dto.PaymentResponseDto;
import com.quostomize.quostomize_be.domain.customizer.payment.service.PaymentService;
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
    ResponseEntity<PaymentResponseDto> payment(@RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponseDto = paymentService.payment(paymentRequestDto);
        return ResponseEntity.created(URI.create("/api/payment"+paymentResponseDto.payment_id())).build();
    }



}
