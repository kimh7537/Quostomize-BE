package com.quostomize.quostomize_be.domain.customizer.card.repository;

import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDetailRepository extends JpaRepository<CardDetail, Long> {
    Page<CardDetail> findAll(Pageable pageable);
    Page<CardDetail> findByStatus(Pageable pageable, CardStatus status);
    @Query("select c from CardDetail c where c.cardNumber like concat('%', :searchTerm) ")
    Page<CardDetail> findBySearchTerm(Pageable pageable, @Param("searchTerm") String searchTerm);
}