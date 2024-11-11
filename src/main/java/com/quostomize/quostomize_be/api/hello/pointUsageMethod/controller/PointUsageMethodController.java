package com.quostomize.quostomize_be.api.hello.pointUsageMethod.controller;


import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service.PointUsageMethodService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-card")
@RequiredArgsConstructor

public class PointUsageMethodController {

    private final PointUsageMethodService pointUsageMethodService;

    @PostMapping(value = "/toggle/{cardSequenceId}")
    @Operation(summary = "포인트 사용 옵션 변경", description = "포인트 사용 옵션 활성 상태를 변경합니다.(on/off)")
    public ResponseEntity<PointUsageMethod> togglePoingUsage(
            @PathVariable Long cardSequenceId,
            @RequestParam String usageType,
            @RequestParam boolean isActive
    ) {

        PointUsageMethod updatedPointUsageMethod = pointUsageMethodService.togglePointUsage(cardSequenceId,usageType,isActive);

        return ResponseEntity.ok(updatedPointUsageMethod);
    }

}
