package com.quostomize.quostomize_be.domain.customizer.cardapplication.service;

import com.quostomize.quostomize_be.api.card.dto.CreateCardDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDetailsDTO;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.service.CardService;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.entity.CardApplicantInfo;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import com.quostomize.quostomize_be.domain.customizer.point.service.CardPointService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardApplicantInfoService {

    @Lazy
    private final CardService cardService;
    private final CardApplicantInfoRepository cardApplicantInfoRepository;
    private final CardPointService cardPointService;  // 카드 생성 시 카드 포인트 생성

    public List<CardApplicantDetailsDTO> getCardApplicantsList() {
        return cardApplicantInfoRepository.findAll().stream().map(
                CardApplicantDetailsDTO::fromEntity
        ).toList();
    }

    public CardApplicantDetailsDTO getCardApplicantsDetails(Long cardApplicantInfoId) {
        CardApplicantInfo cardApplicantInfo = cardApplicantInfoRepository.findByCardApplicantInfoId(
                cardApplicantInfoId
        ).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카드 신청 내역입니다."));
        return CardApplicantDetailsDTO.fromEntity(cardApplicantInfo);
    }

    @Transactional
    public CardApplicantDetailsDTO createCardApplicant(CardApplicantDTO cardApplicantDTO) {
        // 실제로는 신청 때 바로 카드가 생성되지는 않는다.
        // 암호화는 아직 되어있지 않음
        // 1. 카드 생성
        CreateCardDTO createCardDTO = CreateCardDTO.fromApplicant(cardApplicantDTO);
        CardDetail newCard = cardService.createCard(createCardDTO);

        // 2. 카드 포인트 생성 (초기값 0)
        cardPointService.createCardPoint(newCard.getCardSequenceId());

        // 반환값을 주어야 하나...?
        // 3. 카드 신청 정보 저장
        CardApplicantInfo newCardApplicantInfo = cardApplicantInfoRepository.save(CardApplicantInfo.builder()
                .residenceNumber(cardApplicantDTO.residenceNumber())
                .applicantName(cardApplicantDTO.applicantName())
                .englishName(cardApplicantDTO.englishName())
                .zipCode(cardApplicantDTO.zipCode())
                .shippingAddress(cardApplicantDTO.shippingAddress())
                .shippingDetailAddress(cardApplicantDTO.shippingDetailAddress())
                .applicantEmail(cardApplicantDTO.applicantEmail())
                .phoneNumber(cardApplicantDTO.phoneNumber())
                .homeAddress(cardApplicantDTO.homeAddress())
                .homeDetailAddress(cardApplicantDTO.homeDetailAddress())
                .cardDetail(newCard)
                .build()
        );

        return CardApplicantDetailsDTO.fromEntity(newCardApplicantInfo);
    }
}
