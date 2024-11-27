package com.quostomize.quostomize_be.domain.customizer.adminResponse.repository;

import com.quostomize.quostomize_be.domain.customizer.adminResponse.entity.AdminResponse;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminResponseRepository extends JpaRepository<AdminResponse, Long> {
    Optional<AdminResponse> findByMemberQuestion(MemberQuestion memberQuestion);
    Optional<AdminResponse> findByMemberQuestion_QuestionsSequenceId(Long questionsSequenceId);
}