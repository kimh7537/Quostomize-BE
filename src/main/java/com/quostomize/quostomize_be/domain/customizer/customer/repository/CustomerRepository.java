package com.quostomize.quostomize_be.domain.customizer.customer.repository;

import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByMember_MemberId(Long memberId);

    Optional<Customer> findByCardDetail_CardSequenceId(Long cardSequenceId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Customer c " +
            "LEFT JOIN FETCH c.stockInterests si " +
            "JOIN c.member m " +
            "WHERE m.memberId = :memberId")
    Optional<Customer> findCustomerWithLock(@Param("memberId") Long memberId);

    @Query("SELECT c FROM Customer c JOIN FETCH c.cardDetail WHERE c.member = :member")
    Optional<Customer> findWithCardDetailByMember(@Param("member") Member member);

}
