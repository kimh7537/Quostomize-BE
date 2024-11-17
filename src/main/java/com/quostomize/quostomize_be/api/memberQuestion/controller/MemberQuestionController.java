package com.quostomize.quostomize_be.api.memberQuestion.controller;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.service.MemberQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qnas")
public class MemberQuestionController {
    private final MemberQuestionService memberQuestionService;

    public MemberQuestionController(MemberQuestionService memberQuestionService) {
        this.memberQuestionService = memberQuestionService;
    }

    @PostMapping("")
    @Operation(summary = "문의 등록", description = "로그인한 회원에 한해서만 문의사항을 등록할 수 있습니다.")
    public ResponseEntity<ResponseDTO> createQuestion(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MemberQuestionRequest request) {

        Member member = memberQuestionService.getMemberById(memberId);
        Long questionId = memberQuestionService.createQuestion(request, member);
        return ResponseEntity.ok(new ResponseDTO<>(questionId));
    }
}
