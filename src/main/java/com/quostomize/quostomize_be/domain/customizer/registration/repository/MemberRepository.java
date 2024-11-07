package com.quostomize.quostomize_be.domain.customizer.registration.repository;

import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}