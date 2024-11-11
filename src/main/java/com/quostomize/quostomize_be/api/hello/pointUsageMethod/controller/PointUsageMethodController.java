package com.quostomize.quostomize_be.api.hello.pointUsageMethod.controller;


import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodRequestDto;
import com.quostomize.quostomize_be.api.hello.pointUsageMethod.dto.PointUsageMethodResponseDto;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.Service.PointUsageMethodServiceImpl;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-card")
@RequiredArgsConstructor

public class PointUsageMethodController {

    private final PointUsageMethodServiceImpl pointUsageMethodServiceImpl;
    private final PointUsageMethodRepository pointUsageMethodRepository;

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
    public ResponseEntity<PointUsageMethodResponseDto> togglePointUsage(
            @PathVariable Long cardSequenceId,
            @RequestBody List<PointUsageMethodRequestDto> pointUsageMethodRequestDtos
    ) {
        PointUsageMethod pointUsageMethod = pointUsageMethodServiceImpl.getPointUsageMethod(cardSequenceId);

        // 각 요청에 대해 상태를 변경
        pointUsageMethodRequestDtos.stream().forEach(requestDto -> {
            switch (requestDto.usageType()) {
                case "lotto":
                    pointUsageMethod.setIsLotto(requestDto.isActive());
                    break;
                case "payback":
                    pointUsageMethod.setIsPayback(requestDto.isActive());
                    break;
                case "pieceStock":
                    pointUsageMethod.setIsPieceStock(requestDto.isActive());
                    break;
            }
        });

        // 변경된 상태 저장
        PointUsageMethod updatedPointUsageMethod = pointUsageMethodRepository.save(pointUsageMethod);

        // 응답 DTO 생성
        PointUsageMethodResponseDto responseDto = new PointUsageMethodResponseDto(
                updatedPointUsageMethod.getPointUsageTypeId(),
                updatedPointUsageMethod.getIsLotto(),
                updatedPointUsageMethod.getIsPayback(),
                updatedPointUsageMethod.getIsPieceStock()
        );

        return ResponseEntity.ok(responseDto);
    }



}
