package com.quostomize.quostomize_be.domain.customizer.cardBenefit.service;

import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitRequest;
import com.quostomize.quostomize_be.api.cardBenefit.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDTO;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import com.quostomize.quostomize_be.domain.auth.service.EncryptService;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.cardBenefit.repository.CardBenefitRepository;
import com.quostomize.quostomize_be.domain.customizer.customer.entity.Customer;
import com.quostomize.quostomize_be.domain.customizer.customer.repository.CustomerRepository;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CardBenefitServiceTest {

    @InjectMocks
    private CardBenefitService cardBenefitService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CardBenefitRepository cardBenefitRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EncryptService encryptService;

    @Mock
    private CardDetailRepository cardDetailRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("혜택 내역 조회 성공")
    void findAll() {
        // given
        long memberId = 1L;
        long cardSequenceId = 100L;
        Customer customer = mock(Customer.class);
        CardDetail cardDetail = mock(CardDetail.class);
        Set<CardBenefit> cardBenefits = Set.of(mock(CardBenefit.class));

        when(customerRepository.findByMember_MemberId(memberId)).thenReturn(Optional.of(customer));
        when(customer.getCardDetail()).thenReturn(cardDetail);
        when(cardDetail.getCardSequenceId()).thenReturn(cardSequenceId);
        when(cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true)).thenReturn(cardBenefits);

        // when
        List<CardBenefitResponse> responses = cardBenefitService.findAll(memberId);

        // then
        assertThat(responses).isNotEmpty();
        verify(customerRepository).findByMember_MemberId(memberId);
        verify(cardBenefitRepository).findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true);
    }

    @Test
    @DisplayName("혜택 변경 가능일자 계산 성공")
    void getBenefitChangeDate() {
        // given
        CardBenefit cardBenefit = mock(CardBenefit.class);
        LocalDateTime modifiedAt = LocalDateTime.now().minusDays(35);
        when(cardBenefit.getModifiedAt()).thenReturn(modifiedAt);

        // when
        Boolean isChangeable = cardBenefitService.getBenefitChangeDate(cardBenefit);

        // then
        assertThat(isChangeable).isTrue();
    }

    @Test
    @DisplayName("변경 일자에 따른 버튼 내용 - 변경하기")
    void getBenefitChangeButtonLabel() {
        // given
        long cardSequenceId = 100L;
        CardBenefit cardBenefit = mock(CardBenefit.class);
        LocalDateTime modifiedAt = LocalDateTime.now().minusDays(31);

        when(cardBenefitRepository.findCardBenefitsByCardDetailCardSequenceIdAndIsActive(cardSequenceId, true)).thenReturn(Set.of(cardBenefit));
        when(cardBenefit.getModifiedAt()).thenReturn(modifiedAt);

        // when
        String buttonLabel = cardBenefitService.getBenefitChangeButtonLabel(cardSequenceId);

        // then
        assertThat(buttonLabel).isEqualTo("변경하기");
    }

    @Test
    @DisplayName("혜택 변경 실패 - 카드 정보 없음")
    void updateCardBenefits_CardNotFound() {
        // given
        Long memberId = 1L;
        List<CardBenefitRequest> requests = List.of(mock(CardBenefitRequest.class));

        when(cardDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cardBenefitService.updateCardBenefits(memberId, requests))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.CARD_DETAIL_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("혜택 변경 및 예약을 하나의 트랜잭션으로 처리 성공")
    void processCardBenefits() {
        // given
        Long memberId = 1L;
        List<CardBenefitRequest> requests = Collections.singletonList(mock(CardBenefitRequest.class));

        doNothing().when(cardBenefitRepository).deactivateCardBenefitsByCardSequenceId(anyLong());

        // when
        cardBenefitService.processCardBenefits(memberId, requests);

        // then
        verify(cardBenefitRepository, times(1)).deactivateCardBenefitsByCardSequenceId(anyLong());
    }

    @Test
    @DisplayName("예약한 혜택 반영 성공")
    void scheduledBenefitUpdate() {
        // given
        LocalDate today = LocalDate.now();
        Set<CardBenefit> benefitsToActivate = Set.of(mock(CardBenefit.class));

        when(cardBenefitRepository.findCardBenefitsByBenefitEffectiveDateAndIsActive(today, false)).thenReturn(benefitsToActivate);

        // when
        cardBenefitService.scheduledBenefitUpdate();

        // then
        verify(cardBenefitRepository).findCardBenefitsByBenefitEffectiveDateAndIsActive(today, false);
        verify(cardBenefitRepository, times(1)).deactivateCardBenefitsByCardSequenceId(anyLong());
        verify(cardBenefitRepository, times(1)).activateBenefitsForToday(today);
    }

    @Test
    @DisplayName("2차 인증번호 확인 실패 - 인증번호 불일치")
    void verifySecondaryAuthCode_InvalidCode() {
        // given
        Long memberId = 1L;
        String inputCode = "wrongCode";
        Member member = mock(Member.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(encryptService.encryptSecondaryAuthCode(inputCode)).thenReturn("encryptedWrongCode");
        when(member.getSecondaryAuthCode()).thenReturn("correctEncryptedCode");

        // when & then
        assertThatThrownBy(() -> cardBenefitService.verifySecondaryAuthCode(memberId, inputCode))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.SECONDARY_AUTH_CODE_NOT_MATCH.getMessage());
    }

    @Test
    @DisplayName("카드 생성 시 혜택 적용 성공")
    void createInitialCardBenefits() {
        // given
        CardDetail cardDetail = mock(CardDetail.class);
        List<CardApplicantDTO.CardBenefitInfo> benefits = List.of(mock(CardApplicantDTO.CardBenefitInfo.class));

        // when
        cardBenefitService.createInitialCardBenefits(cardDetail, benefits);

        // then
        verify(cardBenefitRepository, times(1)).saveAll(anyList());
    }
}
