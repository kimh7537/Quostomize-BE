package com.quostomize.quostomize_be.domain.admin.service;

import com.quostomize.quostomize_be.api.card.dto.CardDetailResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
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

    public Page<CardDetailResponse> getCardDetailsForAdmin(Authentication authentication, int page) {
        String memberRole = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");
        if (!"ROLE_ADMIN".equals(memberRole)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Order.desc("createdAt")));
        return cardService.findPagedCardDetails(pageRequest);
    }
}
