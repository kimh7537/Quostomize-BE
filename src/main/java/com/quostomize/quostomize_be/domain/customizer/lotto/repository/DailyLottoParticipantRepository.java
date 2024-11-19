package com.quostomize.quostomize_be.domain.customizer.lotto.repository;

import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.lotto.entity.DailyLottoParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DailyLottoParticipantRepository extends JpaRepository<DailyLottoParticipant, Long> {
    Optional<DailyLottoParticipant> findByCustomer(Customer customer);

    @Modifying
    @Query(value = "ALTER TABLE daily_lotto_participant AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
