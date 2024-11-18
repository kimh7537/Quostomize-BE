package com.quostomize.quostomize_be.domain.customizer.card.repository;

import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardDetail, Long> {

}
