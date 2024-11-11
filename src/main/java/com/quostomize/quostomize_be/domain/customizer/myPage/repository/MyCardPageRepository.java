package com.quostomize.quostomize_be.domain.customizer.myPage.repository;


import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyCardPageRepository extends JpaRepository<Member, Long> {
    Optional<Customer> findMemberId(Long memberId);
}

