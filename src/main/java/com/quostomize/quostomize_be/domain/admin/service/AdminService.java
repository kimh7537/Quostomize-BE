package com.quostomize.quostomize_be.domain.admin.service;

import com.quostomize.quostomize_be.api.auth.dto.MemberResponse;
import com.quostomize.quostomize_be.api.auth.dto.MemberRoleRequest;
import com.quostomize.quostomize_be.api.card.dto.CardDetailResponse;
import com.quostomize.quostomize_be.api.card.dto.CardStatusRequest;
import com.quostomize.quostomize_be.api.payment.dto.PaymentRecordResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;
import com.quostomize.quostomize_be.domain.auth.service.MemberService;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import com.quostomize.quostomize_be.domain.customizer.card.service.CardService;
import com.quostomize.quostomize_be.domain.customizer.payment.entity.PaymentRecord;
import com.quostomize.quostomize_be.domain.customizer.payment.enums.RecordSearchType;
import com.quostomize.quostomize_be.domain.customizer.payment.service.PaymentRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
public class AdminService {

    private final CardService cardService;
    private final MemberService memberService;
    private final PaymentRecordService paymentRecordService;

    public AdminService(CardService cardService, MemberService memberService, PaymentRecordService paymentRecordService) {
        this.cardService = cardService;
        this.memberService = memberService;
        this.paymentRecordService = paymentRecordService;
    }
    
    // 카드
    public Page<CardDetailResponse> getFilteredCards(Authentication auth, int page, String sortDirection, CardStatus status) {
        validateAdmin(auth);
        Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 20, sort);
        if (status == null) {
            return cardService.getPagedCards(pageable).map(this::convertCardResponse);
        }
        return cardService.getStatusCards(pageable, status).map(this::convertCardResponse);
    }

    public Page<CardDetailResponse> getSearchCards(Authentication auth, int page, String searchTerm) {
        validateAdmin(auth);
        Pageable pageable = PageRequest.of(page, 20, Sort.by("createdAt").descending());
        return cardService.getCardBySearchTerm(pageable, searchTerm).map(this::convertCardResponse);
    }

    public Page<CardDetailResponse> getMemberIdCards(Authentication auth, int page, Long memberId) {
        validateAdmin(auth);
        Pageable pageable = PageRequest.of(page, 20, Sort.by("createdAt").descending());
        return cardService.getCardByMemberId(pageable, memberId).map(this::convertCardResponse);
    }

    @Transactional
    public void updateCardStatus(@AuthenticationPrincipal Long adminId, CardStatusRequest request) {
        cardService.verifySecondaryAuthCode(adminId, request.secondaryAuthCode());
        cardService.updateCardStatus(request);
    }

    // 멤버
    public Page<MemberResponse> getFilteredMembers(Authentication auth, int page, String sortDirection, List<MemberRole> roles) {
        validateAdmin(auth);
        Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 20, sort);
        if (roles == null || roles.isEmpty()) {
            return memberService.getPagedMembers(pageable).map(this::convertMemberResponse);
        }
        return memberService.getRoleMembers(pageable, roles).map(this::convertMemberResponse);
    }

    public Page<MemberResponse> getSearchMembers(Authentication auth, int page, String searchTerm) {
        validateAdmin(auth);
        Pageable pageable = PageRequest.of(page, 20, Sort.by("createdAt").descending());
        if (isNumeric(searchTerm)) {
            Long memberId = Long.parseLong(searchTerm);
            return memberService.getMemberByMemberId(pageable, memberId).map(this::convertMemberResponse);
        }
        return memberService.getMemberByLoginId(pageable, searchTerm).map(this::convertMemberResponse);
    }

    @Transactional
    public void updateMemberRole(@AuthenticationPrincipal Long adminId, MemberRoleRequest request) {
        memberService.verifySecondaryAuthCode(adminId, request.secondaryAuthCode());
        memberService.updateMemberRole(request);
    }

    // 결제내역
    public Page<PaymentRecordResponse> getFilteredRecords(Authentication auth, int page, String sortDirection) {
        validateAdmin(auth);
        Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 20, sort);
        return paymentRecordService.getPagedRecords(pageable).map(this::convertRecordsResponse);
    }

    public Page<PaymentRecordResponse> getSearchRecords(Authentication auth, int page, Long searchAmount, RecordSearchType recordSearchType) {
        validateAdmin(auth);
        Pageable pageable = PageRequest.of(page, 20, Sort.by("createdAt").descending());
        Page<PaymentRecord> records = switch (recordSearchType) {
            case GREATER -> paymentRecordService.getGreaterRecords(pageable, searchAmount);
            case LESS -> paymentRecordService.getLessRecords(pageable, searchAmount);
            case EQUAL -> paymentRecordService.getEqualRecords(pageable, searchAmount);
        };
        return records.map(this::convertRecordsResponse);
    }
    
    // 공통
    public CardDetailResponse convertCardResponse(CardDetail card) {
        return new CardDetailResponse(
                card.getCardSequenceId(),
                card.getCardNumber(),
                card.getCardBrand(),
                card.getIsAppCard(),
                card.getIsForeignBlocked(),
                card.getIsPostpaidTransport(),
                card.getExpirationDate(),
                card.getOptionalTerms(),
                card.getPaymentReceiptMethods(),
                card.getStatus(),
                card.getCreatedAt(),
                card.getModifiedAt()
        );
    }

    public MemberResponse convertMemberResponse(Member member) {
        return new MemberResponse(
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberLoginId(),
                member.getZipCode(),
                member.getMemberAddress(),
                member.getMemberDetailAddress(),
                member.getRole(),
                member.getCreatedAt(),
                member.getModifiedAt()
        );
    }

    public PaymentRecordResponse convertRecordsResponse(PaymentRecord record) {
        return new PaymentRecordResponse(
                record.getPaymentRecordId(),
                record.getIndustryType(),
                record.getBusinessRegistrationNumber(),
                record.getTotalPaymentAmount(),
                record.getCardDetail().getCardSequenceId(),
                record.getCreatedAt(),
                record.getModifiedAt()
        );
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void validateAdmin(Authentication auth) {
        String memberRole = auth.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");
        if (!"ROLE_ADMIN".equals(memberRole)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }
}
