package com.quostomize.quostomize_be.domain.customizer.adminResponse.repository;

import com.quostomize.quostomize_be.domain.customizer.adminResponse.entity.AdminResponse;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminResponseRepository extends JpaRepository<AdminResponse, Long> {
    // 특정 문의글에 대한 답변 존재 여부 확인
    Optional<AdminResponse> findByMemberQuestion(MemberQuestion memberQuestion);
}