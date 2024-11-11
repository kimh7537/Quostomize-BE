package com.quostomize.quostomize_be.api.hello.pointUsageMethod.controller;


import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodRequestDto;
import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodResponseDto;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service.PointUsageMethodServiceImpl;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-card")
@RequiredArgsConstructor

public class PointUsageMethodController {

    private final PointUsageMethodServiceImpl pointUsageMethodServiceImpl;

    @GetMapping(value = "/{cardSequenceId}")
    @Operation(summary = "포인트 사용 옵션 조회", description = "카드 생성시 설정된 포인트 사용 옵션을 조회한다.")
    public ResponseEntity<PointUsageMethodResponseDto> getPointUsageMethod(
            @PathVariable Long cardSequenceId
    ) {
        PointUsageMethod pointUsageMethod = pointUsageMethodServiceImpl.getPointUsageMethod(cardSequenceId);
        PointUsageMethodResponseDto responseDto = new PointUsageMethodResponseDto(
                pointUsageMethod.getPointUsageTypeId(),
                pointUsageMethod.getIsLotto(),
                pointUsageMethod.getIsPayback(),
                pointUsageMethod.getIsPieceStock()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/{cardSequenceId}")
    @Operation(summary = "포인트 사용 옵션 변경", description = "포인트 사용 옵션 활성 상태를 변경합니다.(on/off)")
    public ResponseEntity<PointUsageMethod> togglePoingUsage(
            @PathVariable Long cardSequenceId,
            @RequestBody PointUsageMethodRequestDto pointUsageMethodRequestDto
    ) {
        PointUsageMethod updatedPointUsageMethod = pointUsageMethodServiceImpl.togglePointUsage(
                cardSequenceId, pointUsageMethodRequestDto.usageType(), pointUsageMethodRequestDto.isActive());

        return ResponseEntity.ok(updatedPointUsageMethod);
    }


}
