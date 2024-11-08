package com.quostomize.quostomize_be.domain.customizer.card.repository;

import com.quostomize.quostomize_be.domain.customizer.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);
}
