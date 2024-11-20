package com.quostomize.quostomize_be.api.pointUsageMethod.controller;


import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodRequestDto;
import com.quostomize.quostomize_be.api.pointUsageMethod.dto.PointUsageMethodResponseDto;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service.PointUsageMethodService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/my-card")
@RequiredArgsConstructor

public class PointUsageMethodController {

    private final PointUsageMethodService pointUsageMethodService;
//    private final LottoService lottoService;

    @GetMapping(value = "/{cardSequenceId}") //로그인 하면 변경?
    @Operation(summary = "포인트 사용 옵션 조회", description = "카드 생성시 설정된 포인트 사용 옵션을 조회한다.")
    public ResponseEntity<ResponseDTO> getPointUsageMethod(
            @PathVariable Long cardSequenceId
    ) {

        PointUsageMethod pointUsageMethod = pointUsageMethodService.getPointUsageMethod(cardSequenceId);
        PointUsageMethodResponseDto responseDto = PointUsageMethodResponseDto.from(pointUsageMethod);

        ResponseDTO<PointUsageMethodResponseDto> response = new ResponseDTO(responseDto);

        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/{cardSequenceId}")
    @Operation(summary = "포인트 사용 옵션 변경", description = "포인트 사용 옵션 활성 상태를 변경합니다.(on/off)")
    public ResponseEntity<ResponseDTO> togglePointUsage(
            @PathVariable Long cardSequenceId,
            @RequestBody PointUsageMethodRequestDto pointUsageMethodRequestDto
    ) {
        Boolean isActive = pointUsageMethodRequestDto.isActive();
        String usageType = pointUsageMethodRequestDto.usageType();

        PointUsageMethod updatedPointUsageMethod = pointUsageMethodService.togglePointUsage(
                cardSequenceId, usageType, isActive);
        PointUsageMethodResponseDto responseDto = PointUsageMethodResponseDto.from(updatedPointUsageMethod);
        ResponseDTO<PointUsageMethodResponseDto> response = new ResponseDTO(responseDto);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/change-lotto")
//    @Operation(summary = "로또 참여 설정 변경", description = "나의 카드페이지에서 로또 참여 여부를 ON/OFF 시 참여자 수가 변경됩니다.")
//    public ResponseEntity<String> toggleLottoParticipation(@Valid @RequestBody LottoParticipantRequestDto request) {
//        lottoService.toggleLottoParticipation(request);
//        return ResponseEntity.ok("로또 참여 설정이 변경되었습니다.");
//    }
}
