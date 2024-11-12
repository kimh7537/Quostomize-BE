package com.quostomize.quostomize_be.api.memberQuestion.controller;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;


import com.quostomize.quostomize_be.api.memberQuestion.dto.PageMemberQuestionResponse;
import com.quostomize.quostomize_be.api.memberQuestion.dto.PageResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.service.MemberQuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qnas")
public class MemberQuestionController {

    private final MemberQuestionService memberQuestionService;

    public MemberQuestionController(MemberQuestionService memberQuestionService) {
        this.memberQuestionService = memberQuestionService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseDTO> getAllMemberQuestions(@RequestParam(defaultValue = "0") int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("questionsSequenceId"))); // 최신순으로 10개씩 페이지네이션
        Page<PageMemberQuestionResponse> questions = memberQuestionService.getAllMemberQuestions(pageRequest);
        PageResponse pageResponse = new PageResponse(
                questions.getContent(), // 현재 페이지에 해당하는 데이터 리스트
                questions.getNumber(), // 현재 페이지 번호
                questions.getTotalPages(), // 총 페이지 수
                questions.getSize(), // 페이지당 데이터 수
                questions.getTotalElements() // 전체 데이터 수
        );
        return ResponseEntity.ok(new ResponseDTO<>(pageResponse));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO> createQuestion(
            @PathVariable Long id,
            @RequestBody MemberQuestionRequest request) {
        Member member = memberQuestionService.getMemberById(id);
        Long questionId = memberQuestionService.createQuestion(request, member);
        return ResponseEntity.ok(new ResponseDTO<>(questionId));
    }
}
