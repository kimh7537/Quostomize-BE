package com.quostomize.quostomize_be.domain.customizer.lotto.repository;

import com.quostomize.quostomize_be.domain.customizer.lotto.entity.LottoWinnerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LottoWinnerRecordRepository extends JpaRepository<LottoWinnerRecord, Long> {
    List<LottoWinnerRecord> findByLottoDate(LocalDate lottoDate);
}