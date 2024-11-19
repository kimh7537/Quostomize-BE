package com.quostomize.quostomize_be.domain.customizer.customer.repository;

import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByMember_MemberId(Long memberId);
}
