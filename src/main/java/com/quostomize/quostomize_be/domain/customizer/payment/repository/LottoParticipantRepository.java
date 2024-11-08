package com.quostomize.quostomize_be.domain.customizer.payment.repository;

import com.quostomize.quostomize_be.domain.customizer.payment.entity.LottoParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LottoParticipantRepository extends JpaRepository<LottoParticipant, Long> {
}
