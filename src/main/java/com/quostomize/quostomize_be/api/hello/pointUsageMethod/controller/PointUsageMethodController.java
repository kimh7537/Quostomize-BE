package com.quostomize.quostomize_be.api.hello.pointUsageMethod.controller;


import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodRequestDto;
import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodResponseDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service.PointUsageMethodService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/my-card")
@RequiredArgsConstructor

public class PointUsageMethodController {

    private final PointUsageMethodService pointUsageMethodService;
    private final PointUsageMethodRepository pointUsageMethodRepository;

    @GetMapping(value = "/{cardSequenceId}") //로그인 하면 변경?
    @Operation(summary = "포인트 사용 옵션 조회", description = "카드 생성시 설정된 포인트 사용 옵션을 조회한다.")
    public ResponseEntity<PointUsageMethodResponseDto> getPointUsageMethod(
            @PathVariable Long cardSequenceId
    ) {

        PointUsageMethod pointUsageMethod = pointUsageMethodService.getPointUsageMethod(cardSequenceId);
        PointUsageMethodResponseDto responseDto = PointUsageMethodResponseDto.from(pointUsageMethod);

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping(value = "/{cardSequenceId}")
    @Operation(summary = "포인트 사용 옵션 변경", description = "포인트 사용 옵션 활성 상태를 변경합니다.(on/off)")
    public ResponseEntity<PointUsageMethodResponseDto> togglePointUsage(
            @PathVariable Long cardSequenceId,
            @RequestBody PointUsageMethodRequestDto pointUsageMethodRequestDto
    ) {
        // 활성화 상태 결정
        Boolean isActive = determineIsActive(pointUsageMethodRequestDto);

        // 포인트 사용 방법 업데이트
        PointUsageMethod updatedPointUsageMethod = pointUsageMethodService.togglePointUsage(
                cardSequenceId, pointUsageMethodRequestDto.pointUsageTypeId(), isActive);

        // Response DTO 생성
        PointUsageMethodResponseDto responseDto = PointUsageMethodResponseDto.from(updatedPointUsageMethod);

        // ResponseEntity 반환
        return ResponseEntity.ok(responseDto);
    }

}
