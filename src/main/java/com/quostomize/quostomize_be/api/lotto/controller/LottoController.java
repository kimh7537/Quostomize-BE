package com.quostomize.quostomize_be.api.lotto.controller;

import com.quostomize.quostomize_be.api.lotto.dto.LottoParticipantResponseDto;
import com.quostomize.quostomize_be.api.lotto.dto.LottoWinnerResponseDto;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.DailyLottoWinnerService;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.LottoService;
import com.quostomize.quostomize_be.domain.customizer.lotto.service.LottoWinnerRecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/api/lottery")
@RequiredArgsConstructor
public class LottoController {

    private final LottoService lottoService;
    private final DailyLottoWinnerService dailyLottoWinnerService;
    private final LottoWinnerRecordService lottoWinnerRecordService;

    @PostMapping("")
    @Operation(summary = "로또 참여자 테이블에 등록", description = "자동으로 로또 참여 조건에 맞는 참여자들을 오늘 참여자 테이블에 등록합니다.")
    public ResponseEntity<ResponseDTO<List<LottoParticipantResponseDto>>> registerLottoParticipants() {
        List<LottoParticipantResponseDto> participantResponseDtos = lottoService.registerLottoParticipants();
        return ResponseEntity.ok(new ResponseDTO<>(participantResponseDtos));
    }

    @GetMapping("")
    @Operation(summary = "총 로또 참여자 수 조회", description = "현재 로또에 참여한 총 참여자 수를 반환합니다.")
    public ResponseEntity<ResponseDTO<Long>> getTotalLottoParticipantsCount() {
        Long totalParticipants = lottoService.getTotalLottoParticipantsCount();
        return ResponseEntity.ok(new ResponseDTO<>(totalParticipants));
    }

    @GetMapping("/today-winner")
    @Operation(summary = "오늘의 로또 당첨자 조회", description = "오늘 날짜의 로또 당첨자 목록을 조회합니다.")
    public ResponseEntity<ResponseDTO<List<LottoWinnerResponseDto>>> getDailyLottoWinners() {
        List<LottoWinnerResponseDto> winners = dailyLottoWinnerService.getDailyLottoWinners();
        return ResponseEntity.ok(new ResponseDTO<>(winners));
    }

    @GetMapping("/past-winner")
    @Operation(summary = "과거 로또 당첨자 조회", description = "선택한 날짜의 로또 당첨자 목록을 조회합니다.")
    public ResponseEntity<ResponseDTO<List<LottoWinnerResponseDto>>> getLottoWinnersByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<LottoWinnerResponseDto> pastWinners = lottoWinnerRecordService.getLottoWinnersByDate(date);
        return ResponseEntity.ok(new ResponseDTO<>(pastWinners));
    }
}
