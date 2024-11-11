package com.quostomize.quostomize_be.domain.customizer.customer.repository;

import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByCardDetail_CardSequenceId(Long cardSequenceId);
}
