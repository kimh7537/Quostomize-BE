package com.quostomize.quostomize_be.memberQuestion.service;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.memberQuestion.entity.MemberQuestion;
import com.quostomize.quostomize_be.memberQuestion.repository.MemberQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberQuestionService {

    private final MemberRepository memberRepository;
    private final MemberQuestionRepository memberQuestionRepository;

    public MemberQuestionService(MemberQuestionRepository memberQuestionRepository, MemberRepository memberRepository) {
        this.memberQuestionRepository = memberQuestionRepository;
        this.memberRepository = memberRepository;
    }

    public Long createQuestion(MemberQuestionRequest request, Member member) {
        MemberQuestion memberQuestion = MemberQuestion.builder()
                .isPrivate(request.isPrivate())
                .isAnswered(false)
                .categoryCode(request.categoryCode())
                .questionTitle(request.questionTitle())
                .questionContent(request.questionContent())
                .member(member)
                .build();

        MemberQuestion savedMemberQuestion = memberQuestionRepository.save(memberQuestion);
        return savedMemberQuestion.getQuestionsSequenceId();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // TODO: 로그인 여부 확인 로직 추가 필요
}
