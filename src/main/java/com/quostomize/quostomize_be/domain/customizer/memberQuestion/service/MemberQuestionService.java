package com.quostomize.quostomize_be.domain.customizer.memberQuestion.service;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;
import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionResponse;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQuestionService {

    private final MemberRepository memberRepository;
    private final MemberQuestionRepository memberQuestionRepository;

    public MemberQuestionService(MemberQuestionRepository memberQuestionRepository, MemberRepository memberRepository) {
        this.memberQuestionRepository = memberQuestionRepository;
        this.memberRepository = memberRepository;
    }

    // TODO: Role 확인 반복 로직은 추후 분리 예정
    // qna 문의글 전체 조회
    public Page<PageMemberQuestionResponse> getAllMemberQuestions(Long memberId, String memberRole, PageRequest pageRequest) {
        Page<MemberQuestion> questionPage;
        // member_role별 조회 로직 분기
        if ("ROLE_ADMIN".equals(memberRole)) {
            questionPage = memberQuestionRepository.findAll(pageRequest);
        } else {
            questionPage = memberQuestionRepository.findByMember_MemberId(memberId, pageRequest);
        }
        return questionPage.map(question -> new PageMemberQuestionResponse(
                question.getQuestionsSequenceId(),
                question.getIsPrivate(),
                question.getIsAnswered(),
                question.getCategoryCode(),
                question.getQuestionTitle()
        ));
    }

    // qna 문의글 사용자별 상세 조회
    public MemberQuestionResponse getMemberQuestion(Long memberId, String memberRole, Long memberQuestionId) {
        MemberQuestion question;
        if ("ROLE_ADMIN".equals(memberRole)) {
            question = memberQuestionRepository.findById(memberQuestionId).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        } else {
            question = memberQuestionRepository.findByMember_MemberIdAndQuestionsSequenceId(memberId, memberQuestionId).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        }

        return new MemberQuestionResponse(
                question.getQuestionsSequenceId(),
                question.getIsAnswered(),
                question.getCategoryCode(),
                question.getQuestionTitle(),
                question.getQuestionContent(),
                question.getMember().getMemberId()
        );
    }

    // TODO: qna 문의글에 대한 답변 내용 확인

    // qna 문의 생성
    @Transactional
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
