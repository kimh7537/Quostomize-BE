package com.quostomize.quostomize_be.domain.customizer.cardapplication.service;

import com.quostomize.quostomize_be.api.card.dto.CreateCardDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDetailsDTO;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.auth.service.EncryptService;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.service.CardService;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.service.CardBenefitService;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.entity.CardApplicantInfo;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.customizer.point.service.CardPointService;
import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service.PointUsageMethodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardApplicantInfoServiceTest {

    @InjectMocks
    private CardApplicantInfoService cardApplicantInfoService;

    @Mock
    private CardService cardService;

    @Mock
    private CardApplicantInfoRepository cardApplicantInfoRepository;

    @Mock
    private CardPointService cardPointService;

    @Mock
    private PointUsageMethodService pointUsageMethodService;

    @Mock
    private CardBenefitService cardBenefitService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EncryptService encryptService;

    @Test
    @DisplayName("카드 신청 성공")
    void createCardApplicantSuccess() {
        // Given
        CardApplicantDTO requestDto = createTestCardApplicantDTO();
        CardDetail cardDetail = createTestCardDetail();
        CardApplicantInfo cardApplicantInfo = createTestCardApplicantInfo(cardDetail);
        Member member = createTestMember();

        when(cardService.createCard(any(CreateCardDTO.class))).thenReturn(cardDetail);
        when(cardApplicantInfoRepository.save(any(CardApplicantInfo.class))).thenReturn(cardApplicantInfo);
        when(encryptService.encryptResidenceNumber(anyString())).thenReturn("encrypted");
        when(memberRepository.findByResidenceNumber(anyString())).thenReturn(Optional.of(member));
        when(cardPointService.createCardPoint(anyLong())).thenReturn(null);
        when(pointUsageMethodService.createPointUsageMethod(anyLong(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer());

        // When
        CardApplicantDetailsDTO result = cardApplicantInfoService.createCardApplicant(requestDto);

        // Then
        assertNotNull(result);
        assertEquals("홍길동", result.applicantName());
        assertEquals("HONGGILDONG", result.englishName());

        verify(cardService).createCard(any(CreateCardDTO.class));
        verify(cardApplicantInfoRepository).save(any(CardApplicantInfo.class));
        verify(cardPointService).createCardPoint(anyLong());
        verify(pointUsageMethodService).createPointUsageMethod(anyLong(), anyBoolean(), anyBoolean(), anyBoolean());
        verify(cardBenefitService).createInitialCardBenefits(any(), any());
        verify(customerRepository).save(any(Customer.class));
    }

    private CardApplicantDTO createTestCardApplicantDTO() {
        List<CardApplicantDTO.CardBenefitInfo> benefits = List.of(
                new CardApplicantDTO.CardBenefitInfo(1L, 101L, 4),
                new CardApplicantDTO.CardBenefitInfo(2L, 200L, 4),
                new CardApplicantDTO.CardBenefitInfo(3L, 301L, 4),
                new CardApplicantDTO.CardBenefitInfo(4L, 402L, 4),
                new CardApplicantDTO.CardBenefitInfo(5L, null, 3)
        );

        return new CardApplicantDTO(
                1, 1, true, false, true,
                "123456", "123456", 1, 1,
                benefits,
                true, false, false,
                "9905051234567", "홍길동", "HONGGILDONG",
                "12345", "서울시 강남구", "테헤란로 123",
                "test@example.com", "01012345678",
                "서울시 강남구", "테헤란로 123"
        );
    }

    private CardDetail createTestCardDetail() {
        return CardDetail.builder()
                .cardSequenceId(1L)
                .cardNumber("1234567890123456")
                .cardPassword("123456")
                .cvcNumber("123")
                .build();
    }

    private CardApplicantInfo createTestCardApplicantInfo(CardDetail cardDetail) {
        return CardApplicantInfo.builder()
                .residenceNumber("9905051234567")
                .applicantName("홍길동")
                .englishName("HONGGILDONG")
                .zipCode("12345")
                .shippingAddress("서울시 강남구")
                .shippingDetailAddress("테헤란로 123")
                .applicantEmail("test@example.com")
                .phoneNumber("01012345678")
                .homeAddress("서울시 강남구")
                .homeDetailAddress("테헤란로 123")
                .cardDetail(cardDetail)
                .build();
    }

    private Member createTestMember() {
        return Member.builder()
                .memberName("홍길동")
                .memberEmail("test@example.com")
                .memberLoginId("testuser")
                .memberPassword("password")
                .residenceNumber("encrypted_990505-1234567")
                .build();
    }
}