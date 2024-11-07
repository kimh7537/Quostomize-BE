package com.quostomize.quostomize_be.api.cardBenefit.controller;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.service.CardBenefitService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/benefit-change")
@RequiredArgsConstructor
public class CardBenefitController {
    private final CardBenefitService cardBenefitService;

    @GetMapping("/benefit-status")
    @Operation(summary = "카드 혜택 내역 조회", description = "로그인한 고객의 카드 혜택 내역 조회 결과를 제공합니다.")
    public ResponseEntity<List<CardBenefitResponse>> getCardBenefitStatus() {
        List<CardBenefitResponse> benefits = cardBenefitService.findAll();
        return ResponseEntity.ok(benefits);
    }

    // TODO: 혜택 변경 적용하기
    
    // TODO: 혜택 변경 예약하기 -> 테이블 분리여부 결정 필요

}