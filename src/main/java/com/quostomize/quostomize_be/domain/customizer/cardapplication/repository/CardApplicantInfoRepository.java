package com.quostomize.quostomize_be.domain.customizer.cardapplication.repository;

import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.entity.CardApplicantInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardApplicantInfoRepository extends JpaRepository<CardApplicantInfo, Long> {
    Optional<CardApplicantInfo> findByCardApplicantInfoId(Long cardApplicationInfoId);
    Optional<CardApplicantInfo> findByResidenceNumber(String residenceNumber);
    @Query("select c from CardApplicantInfo c join c.cardDetail d where d.status = :status")
    Page<CardApplicantInfo> findByCardStatus(@Param("status") CardStatus status, Pageable pageable);
    @Query("""
        select ca from CardApplicantInfo ca
        join fetch ca.cardDetail cd
        join fetch Customer c on c.cardDetail.cardSequenceId = cd.cardSequenceId
        where c.member.memberId = :memberId
""")
    Page<CardApplicantInfo> findCardApplicantByMemberId(Pageable pageable, @Param("memberId") Long memberId);
}
