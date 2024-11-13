package com.quostomize.quostomize_be.domain.customizer.cardApplication.service;

import com.quostomize.quostomize_be.api.card.dto.CreateCardDTO;
import com.quostomize.quostomize_be.api.cardapplicant.CardApplicantDTO;
import com.quostomize.quostomize_be.api.cardapplicant.CardApplicantDetailsDTO;
import com.quostomize.quostomize_be.common.DTO.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.service.CardService;
import com.quostomize.quostomize_be.domain.customizer.cardApplication.entity.CardApplicantInfo;
import com.quostomize.quostomize_be.domain.customizer.cardApplication.repository.CardApplicantInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardApplicantInfoService {

    @Lazy
    private final CardService cardService;

    private final CardApplicantInfoRepository cardApplicantInfoRepository;

    public ResponseDTO<List<CardApplicantDetailsDTO>> getCardApplicantsList() {
        List<CardApplicantDetailsDTO> cardApplicantDetailsDTOList = cardApplicantInfoRepository.findAll().stream().map(
                CardApplicantDetailsDTO::fromEntity
        ).toList();
        return new ResponseDTO<>(cardApplicantDetailsDTOList);
    }

    public ResponseDTO<CardApplicantDetailsDTO> getCardApplicantsDetails(String cardApplicantInfoId) {
        CardApplicantInfo cardApplicantInfo = cardApplicantInfoRepository.findByCardApplicantInfoId(
                Long.parseLong(cardApplicantInfoId)
        ).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카드 신청 내역입니다."));
        return new ResponseDTO<>(CardApplicantDetailsDTO.fromEntity(cardApplicantInfo));
    }

    public ResponseDTO<Void> createCardApplicant(CardApplicantDTO cardApplicantDTO) {
        // 실제로는 신청 때 바로 카드가 생성되지는 않는다.
        // 암호화는 아직 되어있지 않음
        CreateCardDTO createCardDTO = CreateCardDTO.fromApplicant(cardApplicantDTO);
        CardDetail newCard = cardService.createCard(createCardDTO);

        // 반환값을 주어야 하나...?
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

        return new ResponseDTO<>(null);
    }
}
