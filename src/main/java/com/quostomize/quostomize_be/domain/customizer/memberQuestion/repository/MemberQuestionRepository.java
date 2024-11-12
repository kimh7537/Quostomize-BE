package com.quostomize.quostomize_be.domain.customizer.memberQuestion.repository;

import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Integer> {
    Page<MemberQuestion> findAll(Pageable pageable);
}
