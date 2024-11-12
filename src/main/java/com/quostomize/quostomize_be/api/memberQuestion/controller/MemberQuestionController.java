package com.quostomize.quostomize_be.api.memberQuestion.controller;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import com.quostomize.quostomize_be.memberQuestion.service.MemberQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qnas")
public class MemberQuestionController {

    private final MemberQuestionService memberQuestionService;

    public MemberQuestionController(MemberQuestionService memberQuestionService) {
        this.memberQuestionService = memberQuestionService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO> createQuestion(
            @PathVariable Long id,
            @RequestBody MemberQuestionRequest request) {

        Member member = memberQuestionService.getMemberById(id);
        Long questionId = memberQuestionService.createQuestion(request, member);
//        return ResponseEntity.ok(new ResponseDTO<>(response));
        return ResponseEntity.ok(new ResponseDTO<>(questionId));
    }
}
