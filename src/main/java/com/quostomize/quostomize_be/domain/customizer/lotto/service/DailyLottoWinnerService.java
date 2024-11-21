package com.quostomize.quostomize_be.domain.customizer.lotto.service;

import com.quostomize.quostomize_be.api.lotto.dto.LottoWinnerResponseDto;
import com.quostomize.quostomize_be.domain.customizer.lotto.repository.DailyLottoWinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyLottoWinnerService {
    private final DailyLottoWinnerRepository dailyLottoWinnerRepository;

    public List<LottoWinnerResponseDto> getDailyLottoWinners() {
        return dailyLottoWinnerRepository.findAll()
                .stream()
                .map(LottoWinnerResponseDto::from)
                .collect(Collectors.toList());
    }
}