package com.quostomize.quostomize_be.domain.customizer.card.repository;

import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDetailRepository extends JpaRepository<CardDetail, Long> {
    Page<CardDetail> findAll(Pageable pageable);
}