package com.quostomize.quostomize_be.memberQuestion.repository;

import com.quostomize.quostomize_be.memberQuestion.entity.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Integer> {
}
