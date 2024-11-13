package com.quostomize.quostomize_be.domain.customizer.adminResponse.service;

import com.quostomize.quostomize_be.domain.customizer.adminResponse.entity.AdminResponse;
import com.quostomize.quostomize_be.domain.customizer.adminResponse.repository.AdminResponseRepository;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.repository.MemberQuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminResponseService {

    private final AdminResponseRepository adminResponseRepository;
    private final MemberQuestionRepository memberQuestionRepository;

    public AdminResponseService(AdminResponseRepository adminResponseRepository, MemberQuestionRepository memberQuestionRepository) {
        this.adminResponseRepository = adminResponseRepository;
        this.memberQuestionRepository = memberQuestionRepository;
    }

    // 문의에 대한 답변 등록
    public Long createAdminAnswer(Long questionsSequenceId, String responseContent) {
        MemberQuestion memberQuestion = memberQuestionRepository.findById(questionsSequenceId).orElseThrow(() -> new IllegalArgumentException("해당하는 문의글 없음"));
        
        // 해당 문의글에 대한 답변 존재 여부 확인
        adminResponseRepository.findByMemberQuestion(memberQuestion)
                .ifPresent(adminResponse -> {
                    throw new IllegalStateException("이미 답변이 등록된 문의글임");
                });
        
        AdminResponse adminResponse = AdminResponse.builder()
                .responseContent(responseContent)
                .memberQuestion(memberQuestion)
                .build();
        adminResponseRepository.save(adminResponse);
        return adminResponse.getResponseSequenceId();
    }
}
