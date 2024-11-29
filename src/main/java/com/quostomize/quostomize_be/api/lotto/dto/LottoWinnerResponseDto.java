package com.quostomize.quostomize_be.api.lotto.dto;

import com.quostomize.quostomize_be.domain.customizer.lotto.entity.DailyLottoWinner;
import com.quostomize.quostomize_be.domain.customizer.lotto.entity.LottoWinnerRecord;

import java.time.LocalDate;

public record LottoWinnerResponseDto(
        Long winnerId,
        String customerName,
        LocalDate lottoDate
) {
    public static LottoWinnerResponseDto from(DailyLottoWinner winner) {
        return new LottoWinnerResponseDto(
                winner.getDailyLottoWinnerId(),
                winner.getCustomer().getMember().getMemberName(),
                winner.getLottoDate()
        );
    }

    public static LottoWinnerResponseDto from(LottoWinnerRecord record) {
        return new LottoWinnerResponseDto(
                record.getLottoWinnerRecordId(),
                record.getCustomer().getMember().getMemberName(),
                record.getLottoDate()
        );
    }
}