package com.quostomize.quostomize_be.api.admin.controller;

import com.quostomize.quostomize_be.api.admin.dto.PageAdminResponse;
import com.quostomize.quostomize_be.api.card.dto.CardDetailResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.admin.service.AdminService;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
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
    @Operation(summary = "모든 카드 조회", description = "ADMIN은 필터 및 정렬 옵션을 사용하여 모든 카드를 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getCardInfo(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String status) {
        CardStatus cardStatus = null;
        if (status != null && !status.isEmpty()) {
            cardStatus = CardStatus.fromKey(status);
        }
        Page<CardDetailResponse> cards = adminService.getFilteredCards(auth, page, sortDirection, cardStatus);
        PageAdminResponse response = new PageAdminResponse(cards);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

    @GetMapping("/card-search")
    @Operation(summary = "모든 카드 검색", description = "ADMIN은 cardNumber로 검색하여 모든 카드를 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> search(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String searchTerm
    ) {
        Page<CardDetailResponse> cards = adminService.getSearchCards(auth, page, searchTerm);
        PageAdminResponse response = new PageAdminResponse(cards);
        return ResponseEntity.ok(new ResponseDTO(response));
    }
}
