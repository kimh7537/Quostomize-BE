package com.quostomize.quostomize_be.domain.customizer.lotto.repository;

import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.lotto.entity.DailyLottoParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLottoParticipantRepository extends JpaRepository<DailyLottoParticipant, Long> {
    Optional<DailyLottoParticipant> findByCustomer(Customer customer);
}
