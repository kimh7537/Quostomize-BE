package com.quostomize.quostomize_be.api.pointUsageMethod.controller;


import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodResponse;
import com.quostomize.quostomize_be.api.pointUsageMethod.dto.*;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service.PointUsageMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/api/my-card")
@RequiredArgsConstructor
@Tag(name = "나의 카드 페이지 관련 API", description = "나의 카드 관련 API 모음")
public class PointUsageMethodController {
    private final PointUsageMethodService pointUsageMethodService;

    @GetMapping()
    @Operation(summary = "모든 나의 카드 정보 조회", description = "카드 생성시 설정된 나의 카드 정보(이미지, 혜택, 옵션)을 조회한다.")
    public ResponseEntity<ResponseDTO> getMyCardDetails(@AuthenticationPrincipal Long memberId) {
        List<PointUsageMethodResponse> myCardDetails = pointUsageMethodService.getMyCardDetails(memberId);
        return ResponseEntity.ok(new ResponseDTO<>(myCardDetails));
    }

    @PatchMapping(value = "/{cardSequenceId}/lotto")
    @Operation(summary = "일일복권 포인트 사용 옵션 변경", description = "포인트 사용 옵션 활성 상태를 변경합니다.(on/off)")
    public ResponseEntity<ResponseDTO> updateLotto(
            @PathVariable Long cardSequenceId,
            @RequestBody @Valid LottoRequestDto request) {
        pointUsageMethodService.updateLotto(cardSequenceId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{cardSequenceId}/payback")
    @Operation(summary = "페이백 포인트 사용 옵션 변경", description = "포인트 사용 옵션 활성 상태를 변경합니다.(on/off)")
    public ResponseEntity<ResponseDTO> updatePayback(
            @PathVariable Long cardSequenceId,
            @RequestBody @Valid PaybackRequestDto request) {
        pointUsageMethodService.updatePayback(cardSequenceId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{cardSequenceId}/piece-stock")
    @Operation(summary = "조각투자 포인트 사용 옵션 변경", description = "포인트 사용 옵션 활성 상태를 변경합니다.(on/off)")
    public ResponseEntity<ResponseDTO> updatePieceStock(
            @PathVariable Long cardSequenceId,
            @RequestBody @Valid PieceStockRequestDto request) {
        pointUsageMethodService.updatePieceStock(cardSequenceId, request);
        return ResponseEntity.noContent().build();
    }

}
