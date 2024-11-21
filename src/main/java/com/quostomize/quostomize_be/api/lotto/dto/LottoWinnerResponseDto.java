package com.quostomize.quostomize_be.api.lotto.dto;

import com.quostomize.quostomize_be.domain.customizer.lotto.entity.DailyLottoWinner;

import java.time.LocalDate;

public record LottoWinnerResponseDto(
    Long winnerId,
    String customerName,
    LocalDate lottoDate
) {
    public static LottoWinnerResponseDto from(DailyLottoWinner winner) {
        return new LottoWinnerResponseDto(
            winner.getDailyLottoWinnerId(),
            winner.getCustomer().getMember().getMemberName(),  // 고객 이름
            winner.getLottoDate()
        );
    }
}