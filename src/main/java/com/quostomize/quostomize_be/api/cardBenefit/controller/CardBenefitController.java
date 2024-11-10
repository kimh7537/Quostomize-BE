package com.quostomize.quostomize_be.api.cardBenefit.controller;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitRequest;
import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.service.CardBenefitService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/benefit-change")
@RequiredArgsConstructor
public class CardBenefitController {
    private final CardBenefitService cardBenefitService;

    @GetMapping("/benefit-status")
    @Operation(summary = "카드 혜택 내역 조회", description = "로그인한 고객의 현재 적용된 카드 혜택을 조회합니다.")
    public ResponseEntity<List<CardBenefitResponse>> getCardBenefitStatus() {
        List<CardBenefitResponse> benefits = cardBenefitService.findAll();
        return ResponseEntity.ok(benefits);
    }
    
    // TODO: 카드 헤택 변경 적용하기
    @PatchMapping()
    public ResponseEntity<CardBenefitResponse> updateCardBenefitStatus(@RequestBody CardBenefitRequest request) {

    }

    // TODO: 혜택 변경 예약하기

}