package com.quostomize.quostomize_be.api.lotto.controller;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantRequestDto;
import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantResponseDto;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LottoController {

    private final LottoService lottoService;

    @PostMapping("/lottery")
    public List<LottoParticipantResponseDto> registerLottoParticipants() {
        return lottoService.registerLottoParticipants();
    }

    @PostMapping("/my-card/change-lotto")
    public void toggleLottoParticipation(@RequestBody LottoParticipantRequestDto request) {
        lottoService.toggleLottoParticipation(request);
    }

    @GetMapping("/lottery")
    public Long getTotalLottoParticipantsCount() {
        return lottoService.getTotalLottoParticipantsCount();
    }
}
