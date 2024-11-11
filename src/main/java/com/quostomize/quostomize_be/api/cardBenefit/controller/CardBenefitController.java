package com.quostomize.quostomize_be.api.cardBenefit.controller;

import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository.CardBenefitRepository;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.service.CardBenefitService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/benefit-change")
@RequiredArgsConstructor
public class CardBenefitController {
    private final CardBenefitService cardBenefitService;
    private final CardBenefitRepository cardBenefitRepository;

    @GetMapping("/{cardSequenceId}")
    @Operation(summary = "카드 혜택 변경 가능여부 일자 계산", description = "페이지 마운트 시 카드 혜택 변경 가능여부 일자를 계산하여 '변경' 혹은 '예약' 로직을 안내합니다.")
    public ResponseEntity<String> getBenefitChangeDate(@PathVariable Long cardSequenceId) {
        CardBenefit cardBenefit = cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("적용된 혜택 없음 - 카드: " + cardSequenceId));
        String buttonLabel = cardBenefitService.getBenefitChangeButtonLabel(cardBenefit);
        return ResponseEntity.ok(buttonLabel);
    }
}