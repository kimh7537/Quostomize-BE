package com.quostomize.quostomize_be.domain.customizer.memberQuestion.service;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;
import com.quostomize.quostomize_be.api.memberQuestion.dto.PageMemberQuestionResponse;
import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.member.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.repository.MemberQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberQuestionService {

    private final MemberRepository memberRepository;
    private final MemberQuestionRepository memberQuestionRepository;

    public MemberQuestionService(MemberQuestionRepository memberQuestionRepository, MemberRepository memberRepository) {
        this.memberQuestionRepository = memberQuestionRepository;
        this.memberRepository = memberRepository;
    }

    // 문의글 전체 조회
    public Page<PageMemberQuestionResponse> getAllMemberQuestions(PageRequest pageRequest) {
        Page<MemberQuestion> questionPage = memberQuestionRepository.findAll(pageRequest);
        return questionPage.map(question -> new PageMemberQuestionResponse(
                question.getQuestionsSequenceId(),
                question.getIsPrivate(),
                question.getIsAnswered(),
                question.getCategoryCode(),
                question.getQuestionTitle()
        ));
    }

    // 문의글 등록
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
