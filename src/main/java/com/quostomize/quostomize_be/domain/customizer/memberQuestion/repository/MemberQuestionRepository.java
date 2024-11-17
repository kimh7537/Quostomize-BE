package com.quostomize.quostomize_be.domain.customizer.memberQuestion.repository;

import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Integer> {
}
