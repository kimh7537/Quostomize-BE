package com.quostomize.quostomize_be.domain.customizer.lotto.repository;

import com.quostomize.quostomize_be.domain.customizer.lotto.entity.DailyLottoWinner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyLottoWinnerRepository extends JpaRepository<DailyLottoWinner, Long> {
}