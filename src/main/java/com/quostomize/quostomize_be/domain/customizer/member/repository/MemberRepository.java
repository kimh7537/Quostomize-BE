package com.quostomize.quostomize_be.domain.customizer.member.repository;

import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(long memberId);
}
