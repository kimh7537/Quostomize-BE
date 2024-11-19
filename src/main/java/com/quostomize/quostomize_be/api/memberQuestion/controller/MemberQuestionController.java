package com.quostomize.quostomize_be.api.memberQuestion.controller;

import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionRequest;
import com.quostomize.quostomize_be.api.memberQuestion.dto.MemberQuestionResponse;
import com.quostomize.quostomize_be.api.memberQuestion.dto.PageMemberQuestionResponse;
import com.quostomize.quostomize_be.api.memberQuestion.dto.PageResponse;

import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.service.MemberQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    @GetMapping("")
    @Operation(summary = "전체 문의글 조회", description = "ADMIN은 전체 문의글을 조회, MEMBER/OLD_MEMBER는 본인이 작성한 글만 조회합니다.")
    public ResponseEntity<ResponseDTO> getAllMemberQuestions(Authentication authentication, @RequestParam(defaultValue = "0") int page) {
        Long memberId = (Long) authentication.getPrincipal();
        String memberRole = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("questionsSequenceId"))); // 최신순으로 10개씩 페이지네이션
        Page<PageMemberQuestionResponse> questions = memberQuestionService.getAllMemberQuestions(memberId, memberRole, pageRequest);
        PageResponse pageResponse = new PageResponse(
                questions.getContent(), // 현재 페이지에 해당하는 데이터 리스트
                questions.getNumber(), // 현재 페이지 번호
                questions.getTotalPages(), // 총 페이지 수
                questions.getSize(), // 페이지당 데이터 수
                questions.getTotalElements() // 전체 데이터 수
        );
        return ResponseEntity.ok(new ResponseDTO<>(pageResponse));
    }


    @GetMapping("/{questionId}")
    @Operation(summary = "사용자별 특정 문의글 상세 조회", description = "사용자별로 특정 문의글을 상세 조회합니다.")
    public ResponseEntity<ResponseDTO> getMemberQuestionDetail(Authentication authentication, @PathVariable Long questionId) {
        Long memberId = (Long) authentication.getPrincipal();
        String memberRole = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");
        MemberQuestionResponse memberQuestion = memberQuestionService.getMemberQuestion(memberId, memberRole, questionId);
        return ResponseEntity.ok(new ResponseDTO<>(memberQuestion));
    }
}
