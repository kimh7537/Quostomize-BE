package com.quostomize.quostomize_be.api.admin.controller;

import com.quostomize.quostomize_be.api.admin.dto.PageAdminResponse;
import com.quostomize.quostomize_be.api.card.dto.CardDetailResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/admin")
public class AdminController {

    private final AdminService adminService;

 public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/card-info")
    @Operation(summary = "모든 카드 조회", description = "ADMIN은 모든 카드를 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getCardInfo(
            Authentication authentication, @RequestParam(defaultValue = "0") int page) {
        Page<CardDetailResponse> cards = adminService.getCardDetailsForAdmin(authentication, page);
        PageAdminResponse response = new PageAdminResponse(cards);
        return ResponseEntity.ok(new ResponseDTO(response));
    }
}
