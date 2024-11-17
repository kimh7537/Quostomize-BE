package com.quostomize.quostomize_be.domain.customizer.memberQuestion.service;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;
import com.quostomize.quostomize_be.api.memberQuestion.dto.PageMemberQuestionResponse;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.repository.MemberQuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
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

    // qna 문의 생성
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
        return memberRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MEMBER_INFO_NOT_FOUND));
    }
}
