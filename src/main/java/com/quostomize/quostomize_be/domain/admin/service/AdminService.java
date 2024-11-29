package com.quostomize.quostomize_be.domain.admin.service;

import com.quostomize.quostomize_be.api.card.dto.CardDetailResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import com.quostomize.quostomize_be.domain.customizer.card.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class AdminService {

    private final CardService cardService;

    public AdminService(CardService cardService) {
        this.cardService = cardService;
    }

    public Page<CardDetailResponse> getFilteredCards(Authentication auth, int page, String sortDirection, CardStatus status) {
        validateAdmin(auth);
        Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 20, sort);
        if (status == null) {
            return cardService.getPagedCards(pageable).map(this::convertResponse);
        }
        return cardService.getStatusCards(pageable, status).map(this::convertResponse);
    }

    public Page<CardDetailResponse> getSearchCards(Authentication auth, int page, String searchTerm) {
        validateAdmin(auth);
        Pageable pageable = PageRequest.of(page, 20, Sort.by("createdAt").descending());
        return cardService.getCardBySearchTerm(pageable, searchTerm).map(this::convertResponse);
    }

    public CardDetailResponse convertResponse(CardDetail card) {
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
