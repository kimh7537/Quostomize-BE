package com.quostomize.quostomize_be.api.admin.controller;

import com.quostomize.quostomize_be.api.admin.dto.PageAdminResponse;
import com.quostomize.quostomize_be.api.auth.dto.MemberResponse;
import com.quostomize.quostomize_be.api.card.dto.CardDetailResponse;
import com.quostomize.quostomize_be.api.card.dto.CardStatusRequest;
import com.quostomize.quostomize_be.api.payment.dto.PaymentRecordResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.admin.service.AdminService;
import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import com.quostomize.quostomize_be.domain.customizer.payment.enums.RecordSearchType;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    // 카드
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
    public ResponseEntity<ResponseDTO> getSearchCard(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String searchTerm
    ) {
        Page<CardDetailResponse> cards = adminService.getSearchCards(auth, page, searchTerm);
        PageAdminResponse response = new PageAdminResponse(cards);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

    @GetMapping("/cancel-pending-info")
    @Operation(summary = "해지 대기 카드 조회", description = "ADMIN은 정렬 옵션을 사용하여 해지 대기 카드를 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getCancelPendingInfo(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        CardStatus catdStatus = CardStatus.CANCELLATION_PENDING;
        Page<CardDetailResponse> cards = adminService.getFilteredCards(auth, page, sortDirection, catdStatus);
        PageAdminResponse response = new PageAdminResponse(cards);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

    @GetMapping("/cancel-pending-search")
    @Operation(summary = "해지 대기 카드 검색", description = "ADMIN은 memberId로 해지 대기 카드를 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getCancelPendingSearch(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam Long memberId
    ) {
        Page<CardDetailResponse> cards = adminService.getMemberIdCards(auth, page, memberId);
        PageAdminResponse response = new PageAdminResponse(cards);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

    @PatchMapping("/status-change")
    @Operation(summary = "카드 상태 변경", description = "ADMIN은 카드 상태를 변경할 수 있습니다.")
    public ResponseEntity<ResponseDTO> updateStatus(
            @AuthenticationPrincipal Long memberId,
            @RequestBody CardStatusRequest request
    ) {
        adminService.updateCardStatus(memberId, request);
        return ResponseEntity.noContent().build();
    }
    // TODO: 거절 로직 API 개발 필요

    // 멤버
    @GetMapping("/member-info")
    @Operation(summary = "모든 고객 조회", description = "ADMIN은 필터 및 정렬 옵션을 사용하여 모든 고객을 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getMemberInfo(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String memberRole) {
        MemberRole role = null;
        if (memberRole != null && !memberRole.isEmpty()) {
            role = MemberRole.fromKey(memberRole);
        }
        Page<MemberResponse> members = adminService.getFilteredMembers(auth, page, sortDirection, role);
        PageAdminResponse response = new PageAdminResponse(members);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

    @GetMapping("/member-search")
    @Operation(summary = "모든 고객 검색", description = "ADMIN은 memberLoginId로 검색하여 모든 고객을 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getMemberSearch(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String searchTerm
    ) {
        Page<MemberResponse> members = adminService.getSearchMembers(auth, page, searchTerm);
        PageAdminResponse response = new PageAdminResponse(members);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

    // 결제내역
    @GetMapping("/payment-record-info")
    @Operation(summary = "모든 결제내역 조회", description = "ADMIN은 정렬 옵션을 사용하여 모든 결제내역을 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getPaymentRecordInfo(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Page<PaymentRecordResponse> records = adminService.getFilteredRecords(auth, page, sortDirection);
        PageAdminResponse response = new PageAdminResponse(records);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

    @GetMapping("/payment-record-search")
    @Operation(summary = "모든 결제내역 검색", description = "ADMIN은 입력 금액을 기준으로 이상/이하/동일 totalPaymentAmount를 가진 결제내역을 조회할 수 있습니다.")
    public ResponseEntity<ResponseDTO> getPaymentRecordSearch(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam Long searchAmount,
            @RequestParam(required = false) String searchType
    ) {
        RecordSearchType type = null;
        if (searchType != null && !searchType.isEmpty()) {
            type = RecordSearchType.fromKey(searchType);
        }
        if (type == null) {
            type = RecordSearchType.EQUAL;
        }
        Page<PaymentRecordResponse> records = adminService.getSearchRecords(auth, page, searchAmount, type);
        PageAdminResponse response = new PageAdminResponse(records);
        return ResponseEntity.ok(new ResponseDTO(response));
    }

}
