package com.quostomize.quostomize_be.api.adminResponse.controller;

import com.quostomize.quostomize_be.api.adminResponse.dto.AdminResponseRequest;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.adminResponse.service.AdminResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qnas")
public class AdminResponseController {

    private AdminResponseService adminResponseService;

    public AdminResponseController(AdminResponseService adminResponseService) {
        this.adminResponseService = adminResponseService;
    }

    // 문의에 대한 답변 등록
    @PostMapping("/{questionsSequenceId}/admin-answer")
    // TODO: RequestBody 및 Admin 구분 로직에 Authentication 추가 필요
    public ResponseEntity<ResponseDTO> createAnswer(
            @PathVariable("questionsSequenceId") Long questionsSequenceId,
            @RequestBody AdminResponseRequest adminRequest) {
        boolean isAdmin = true;
        if (!isAdmin) {
            return ResponseEntity.badRequest().body(new ResponseDTO<>("Admin 권한이 필요합니다."));
        }
        adminResponseService.createAdminAnswer(questionsSequenceId, adminRequest.responseContent());
        return ResponseEntity.ok(new ResponseDTO<>(questionsSequenceId));
    }
}
