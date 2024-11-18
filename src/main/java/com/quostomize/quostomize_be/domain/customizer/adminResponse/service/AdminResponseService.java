package com.quostomize.quostomize_be.domain.customizer.adminResponse.service;

import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.adminResponse.entity.AdminResponse;
import com.quostomize.quostomize_be.domain.customizer.adminResponse.repository.AdminResponseRepository;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.repository.MemberQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminResponseService {

    private final AdminResponseRepository adminResponseRepository;
    private final MemberQuestionRepository memberQuestionRepository;

    public AdminResponseService(AdminResponseRepository adminResponseRepository, MemberQuestionRepository memberQuestionRepository) {
        this.adminResponseRepository = adminResponseRepository;
        this.memberQuestionRepository = memberQuestionRepository;
    }

    // TODO: Repository 직접 참조하지 않고, 연관된 Service를 통해서 참조하는 방식으로 변경 필요
    // 답변 조회
    public AdminResponse getAnswer(Long memberId, String memberRole, Long questionsSequenceId) {
        // 관리자: 모든 답변 조회 가능
        if ("ROLE_ADMIN".equals(memberRole)) {
            return adminResponseRepository.findByMemberQuestion_QuestionsSequenceId(questionsSequenceId)
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        }
        
        // 일반 사용자: 자신이 작성한 질문에 대해서만 답변 조회 가능
        MemberQuestion memberQuestion = memberQuestionRepository.findByMember_MemberIdAndQuestionsSequenceId(memberId, questionsSequenceId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        return adminResponseRepository.findByMemberQuestion(memberQuestion)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }


    // 문의에 대한 답변 등록
    @Transactional
    public Long createAdminAnswer(Long memberId, String memberRole, Long questionsSequenceId, String responseContent) {
        // ADMIN 권한 여부 확인
        if (!"ROLE_ADMIN".equals(memberRole)) {
            throw new AppException(ErrorCode.USER_ACCESS_DENIED);
        }

        // 문의글 확인
        MemberQuestion memberQuestion = memberQuestionRepository.findById(questionsSequenceId).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        
        // 답변 중복 여부 확인
        adminResponseRepository.findByMemberQuestion(memberQuestion)
                .ifPresent(adminResponse -> {
                    throw new AppException(ErrorCode.DUPLICATE_REQUEST);
                });
        
        // 답변 저장
        AdminResponse adminResponse = AdminResponse.builder()
                .responseContent(responseContent)
                .memberQuestion(memberQuestion)
                .build();
        adminResponseRepository.save(adminResponse);

        // isAnswered = true로 업데이트
        memberQuestion.markAsAnswered();
        memberQuestionRepository.save(memberQuestion);

        return adminResponse.getResponseSequenceId();
    }
}
