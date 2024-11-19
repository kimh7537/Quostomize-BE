package com.quostomize.quostomize_be.domain.customizer.registration.repository;

import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 회원 가입 시 중복 여부 확인용
    Member findByMemberLoginId(String memberLoginId);
    Member findByMemberPhoneNumber(String memberPhoneNumber);
    Member findByMemberEmail(String memberEmail);
}









