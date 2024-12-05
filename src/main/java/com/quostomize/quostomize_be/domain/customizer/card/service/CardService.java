package com.quostomize.quostomize_be.domain.customizer.card.service;

import com.quostomize.quostomize_be.api.card.dto.CardStatusRequest;
import com.quostomize.quostomize_be.api.card.dto.CreateCardDTO;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.auth.service.EncryptService;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CardService {

    private final CardDetailRepository cardDetailRepository;
    private final CustomerRepository customerRepository;
    private final MemberRepository memberRepository;
    private final EncryptService encryptService;
    private final Random random = new Random();

//    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public CardDetail createCard(CreateCardDTO createCardDTO) {
        String cardNumber = String.valueOf(random.nextLong(1_000_0000_0000_0000L,10_000_0000_0000_0000L));
        String cvc = String.valueOf(random.nextInt(100, 1000));

        // cardPassword와 cardPasswordConfirm의 값이 같은 지 검증
        if (!createCardDTO.cardPassword().equals(createCardDTO.cardPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

//        String encodedPassword = bCryptPasswordEncoder.encode(createCardDTO.cardPassword());
//        String encodedCvc = bCryptPasswordEncoder.encode(cvc);
        int expiredYear = LocalDate.now().plusYears(5).getYear();
        int expiredMonth = LocalDate.now().getMonthValue();
        LocalDate expiredAt = LocalDate.of(expiredYear,expiredMonth, 1);

        CardDetail cardDetail = CardDetail.builder()
                .cardNumber(cardNumber)
                .cardColor(createCardDTO.cardColor())
                .cardBrand(createCardDTO.cardBrand())
                .isAppCard(createCardDTO.isAppCard())
                .isForeignBlocked(createCardDTO.isForeignBlocked())
                .isPostpaidTransport(createCardDTO.isPostpaidTransport())
//                .cardPassword(encodedPassword)
//                .cvcNumber(encodedCvc)
                .cardPassword(createCardDTO.cardPassword())
                .cvcNumber(cvc)
                .expirationDate(expiredAt)
                .optionalTerms(createCardDTO.optionalTerms())
                .paymentReceiptMethods(createCardDTO.paymentReceiptMethods())
                .build();

        return cardDetailRepository.save(cardDetail);
    }

    public Page<CardDetail> getPagedCards(Pageable pageable) {
        return cardDetailRepository.findAll(pageable);
    }

    public Page<CardDetail> getStatusCards(Pageable pageable, CardStatus status) {
        return cardDetailRepository.findByStatus(pageable, status);
    }

    public Page<CardDetail> getCardBySearchTerm(Pageable pageable, String searchTerm) {
        return cardDetailRepository.findBySearchTerm(pageable, searchTerm);
    }

    public Page<CardDetail> getCardByMemberId(Pageable pageable, Long memberId, CardStatus status) {
        return customerRepository.findCardByMemberId(pageable, memberId, status);
    }

    @Transactional
    public void updateCardStatus(CardStatusRequest request) {
        CardDetail card = cardDetailRepository.findById(request.cardSequenceId())
                        .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));
        if (card.getStatus() == CardStatus.CANCELLED) {
            throw new AppException(ErrorCode.CARD_STATUS_CHANGE_NOT_ALLOWED);
        }
        if (!isValidStatus(card.getStatus(), request.status())) {
            throw new AppException(ErrorCode.CARD_STATUS_CHANGE_NOT_ALLOWED);
        }
        cardDetailRepository.updateStatus(request.status(), request.cardSequenceId());
    }

    @Transactional
    public void verifySecondaryAuthCode(Long adminId, String secondaryAuthCode) {
        String storedEncryptedCode = memberRepository.findSecondaryAuthCodeById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_INFO_NOT_FOUND));
        String encryptedInputCode = encryptService.encryptSecondaryAuthCode(secondaryAuthCode);
        if (!encryptedInputCode.equals(storedEncryptedCode)) {
            throw new AppException(ErrorCode.SECONDARY_AUTH_CODE_NOT_MATCH);
        }
    }

    private boolean isValidStatus(CardStatus currentStatus, CardStatus newStatus) {
        return currentStatus.transitionTo(newStatus);
    }
}
