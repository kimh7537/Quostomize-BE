package com.quostomize.quostomize_be.api.lotto.controller;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantRequestDto;
import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantResponseDto;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LottoController {

    private final LottoService lottoService;

    @PostMapping("/lottery")
    public ResponseEntity<ResponseDTO<List<LottoParticipantResponseDto>>> registerLottoParticipants() {
        List<LottoParticipantResponseDto> participantResponseDtos = lottoService.registerLottoParticipants();
        return ResponseEntity.ok(new ResponseDTO<>(participantResponseDtos));
    }

    @PostMapping("/my-card/change-lotto")
    public ResponseEntity<String> toggleLottoParticipation(@RequestBody LottoParticipantRequestDto request) {
        lottoService.toggleLottoParticipation(request);
        return ResponseEntity.ok("로또 참여 설정이 변경되었습니다.");
    }

    @GetMapping("/lottery")
    public ResponseEntity<Long> getTotalLottoParticipantsCount() {
        Long totalParticipants = lottoService.getTotalLottoParticipantsCount();
        return ResponseEntity.ok(totalParticipants);
    }
}
