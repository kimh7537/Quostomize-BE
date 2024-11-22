package com.quostomize.quostomize_be.domain.customizer.lotto.service;

import com.quostomize.quostomize_be.api.lotto.dto.LottoWinnerResponseDto;
import com.quostomize.quostomize_be.domain.customizer.lotto.entity.LottoWinnerRecord;
import com.quostomize.quostomize_be.domain.customizer.lotto.repository.LottoWinnerRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LottoWinnerRecordService {
    private final LottoWinnerRecordRepository lottoWinnerRecordRepository;

    public List<LottoWinnerResponseDto> getLottoWinnersByDate(LocalDate date) {
        return lottoWinnerRecordRepository.findByLottoDate(date).stream()
                .map(LottoWinnerResponseDto::from)
                .collect(Collectors.toList());
    }
}