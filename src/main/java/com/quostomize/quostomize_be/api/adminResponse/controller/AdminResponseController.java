package com.quostomize.quostomize_be.api.adminResponse.controller;

import com.quostomize.quostomize_be.api.adminResponse.dto.AdminResponseRequest;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.adminResponse.service.AdminResponseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qnas")
public class AdminResponseController {

    private AdminResponseService adminResponseService;

    public AdminResponseController(AdminResponseService adminResponseService) {
        this.adminResponseService = adminResponseService;
    }

    // 문의에 대한 답변 등록
    @PostMapping("/{questionsSequenceId}/answer")
    @Operation(summary = "문의 답변 등록", description = "ADMIN 권한만 답변 등록 가능합니다.")
    public ResponseEntity<ResponseDTO> createAnswer(
            Authentication authentication,
            @PathVariable("questionsSequenceId") Long questionsSequenceId,
            @RequestBody AdminResponseRequest adminRequest) {

        Long memberId = (Long) authentication.getPrincipal();
        String memberRole = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");
        
        Long responseId = adminResponseService.createAdminAnswer(memberId, memberRole, questionsSequenceId, adminRequest.responseContent());
        return ResponseEntity.ok(new ResponseDTO<>(responseId));
    }
}
