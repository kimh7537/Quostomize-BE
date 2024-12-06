package com.quostomize.quostomize_be.domain.customizer.cardapplication.service;

import com.quostomize.quostomize_be.api.card.dto.CreateCardDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.BenefitHierarchyDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDetailsDTO;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.auth.service.EncryptService;
import com.quostomize.quostomize_be.domain.customizer.benefit.repository.BenefitCommonCodeRepository;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.enums.CardStatus;
import com.quostomize.quostomize_be.domain.customizer.card.service.CardService;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.service.CardBenefitService;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.entity.CardApplicantInfo;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.customizer.point.service.CardPointService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service.PointUsageMethodService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardApplicantInfoService {

    @Lazy
    private final CardService cardService;
    private final CardApplicantInfoRepository cardApplicantInfoRepository;
    private final CardPointService cardPointService;  // 카드 생성 시 카드 포인트 생성
    private final PointUsageMethodService pointUsageMethodService;
    private final CardBenefitService cardBenefitService;
    private final MemberRepository memberRepository;
    private final CustomerRepository customerRepository;
    private final EncryptService encryptService;
    private final BenefitCommonCodeRepository benefitCommonCodeRepository;

    public List<BenefitHierarchyDTO> getBenefitInformation(){
        return benefitCommonCodeRepository.findAllBenefitHierarchy();
    }

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
        // 1. 카드 생성
        CardDetail newCard = createCardDetail(cardApplicantDTO);

        // 2. 카드 신청 정보 저장
        CardApplicantInfo newCardApplicantInfo = createCardApplicantInfo(cardApplicantDTO, newCard);

        // 3. 카드 관련 부가 서비스 설정
        setupCardServices(newCard, cardApplicantDTO);

        // 4. 회원-카드 매핑 customer 추가
        mapMemberToCard(cardApplicantDTO.residenceNumber(), newCard);

        return CardApplicantDetailsDTO.fromEntity(newCardApplicantInfo);
    }

    public Page<CardApplicantDetailsDTO> getApplicantsByStatus(CardStatus status, Pageable pageable) {
        return cardApplicantInfoRepository.findByCardStatus(status, pageable)
                .map(CardApplicantDetailsDTO::fromEntity);
    }

    public Page<CardApplicantDetailsDTO> getCardApplicantByMemberId(Pageable pageable, Long memberId, CardStatus status) {
        return cardApplicantInfoRepository.findCardApplicantByMemberId(pageable, memberId, status)
                .map(CardApplicantDetailsDTO::fromEntity);
    }

    private CardDetail createCardDetail(CardApplicantDTO cardApplicantDTO) {
        CreateCardDTO createCardDTO = CreateCardDTO.fromApplicant(cardApplicantDTO);
        return cardService.createCard(createCardDTO);
    }

    private CardApplicantInfo createCardApplicantInfo(CardApplicantDTO dto, CardDetail cardDetail) {
        return cardApplicantInfoRepository.save(CardApplicantInfo.builder()
                .residenceNumber(dto.residenceNumber())
                .applicantName(dto.applicantName())
                .englishName(dto.englishName())
                .zipCode(dto.zipCode())
                .shippingAddress(dto.shippingAddress())
                .shippingDetailAddress(dto.shippingDetailAddress())
                .applicantEmail(dto.applicantEmail())
                .phoneNumber(dto.phoneNumber())
                .homeAddress(dto.homeAddress())
                .homeDetailAddress(dto.homeDetailAddress())
                .cardDetail(cardDetail)
                .build());
    }

    private void setupCardServices(CardDetail cardDetail, CardApplicantDTO dto) {
        // 카드 혜택 설정
        setupCardBenefits(cardDetail, dto.benefits());

        // 카드 포인트 생성
        cardPointService.createCardPoint(cardDetail.getCardSequenceId());

        // 포인트 사용 방법 설정
        setupPointUsageMethod(cardDetail.getCardSequenceId(), dto);
    }

    private void setupCardBenefits(CardDetail cardDetail, List<CardApplicantDTO.CardBenefitInfo> benefits) {
        List<CardApplicantDTO.CardBenefitInfo> cardBenefits = benefits.stream()
                .map(benefit -> new CardApplicantDTO.CardBenefitInfo(
                        benefit.upperCategoryId(),
                        benefit.lowerCategoryId(),
                        benefit.benefitRate()
                ))
                .collect(Collectors.toList());
        cardBenefitService.createInitialCardBenefits(cardDetail, cardBenefits);
    }

    private void setupPointUsageMethod(Long cardSequenceId, CardApplicantDTO dto) {
        pointUsageMethodService.createPointUsageMethod(
                cardSequenceId,
                dto.isPieceStock(),
                dto.isLotto(),
                dto.isPayback()
        );
    }

    private void mapMemberToCard(String residenceNumber, CardDetail cardDetail) {
        String encryptedResidenceNumber = encryptService.encryptResidenceNumber(residenceNumber);
        memberRepository.findByResidenceNumber(encryptedResidenceNumber)
                .ifPresent(member -> {
                    Customer customer = Customer.builder()
                            .member(member)
                            .cardDetail(cardDetail)
                            .build();
                    customerRepository.save(customer);
                });
    }
}
