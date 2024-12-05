package com.quostomize.quostomize_be.api.adminResponse.controller;

import com.quostomize.quostomize_be.api.adminResponse.dto.AdminResponseRequest;
import com.quostomize.quostomize_be.api.adminResponse.dto.AdminResponseResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.adminResponse.entity.AdminResponse;
import com.quostomize.quostomize_be.domain.customizer.adminResponse.service.AdminResponseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/api/qnas")
public class AdminResponseController {

    private AdminResponseService adminResponseService;

    public AdminResponseController(AdminResponseService adminResponseService) {
        this.adminResponseService = adminResponseService;
    }

    @GetMapping("/{questionsSequenceId}/answer")
    @Operation(summary = "답변 내용 확인", description = "ADMIN은 모든 답변을, MEMBER는 자신이 작성한 문의에 대한 답변만 볼 수 있습니다.")
    public ResponseEntity<ResponseDTO> getAnswer(
            Authentication authentication,
            @PathVariable Long questionsSequenceId
    ) {
        Long memberId = (Long) authentication.getPrincipal();
        String memberRole = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");

        Optional<AdminResponse> optionalResponse = adminResponseService.getAnswer(memberId, memberRole, questionsSequenceId);

        // 답변이 존재하지 않는 경우
        if (optionalResponse.isEmpty()) {
            return ResponseEntity.ok(new ResponseDTO<>("답변이 아직 작성되지 않았습니다."));
        }

        AdminResponse response = optionalResponse.get();
        AdminResponseResponse adminResponse = new AdminResponseResponse(
                response.getResponseSequenceId(),
                response.getResponseContent(),
                response.getMemberLoginId()
        );
        return ResponseEntity.ok(new ResponseDTO<>(adminResponse));
    }

    // 문의에 대한 답변 등록
    @PostMapping("/{questionsSequenceId}/submit-answer")
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
