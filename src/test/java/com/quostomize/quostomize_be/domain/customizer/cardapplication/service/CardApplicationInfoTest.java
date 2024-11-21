//package com.quostomize.quostomize_be.domain.customizer.cardapplication.service;
//
//import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDTO;
//import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDetailsDTO;
//import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
//import com.quostomize.quostomize_be.domain.customizer.cardapplication.entity.CardApplicantInfo;
//import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class CardApplicationInfoTest {
//
//
//    @Mock
//    private CardApplicantInfoRepository cardApplicantInfoRepository;
//
//
//    @Test
//    @DisplayName("카드 신청 내역 리스트 가져오기")
//    void getCardApplicationsListTest() {
//        List<CardApplicantDetailsDTO> cardApplicantDetailsDTOList = cardApplicantInfoRepository.findAll().stream().map(
//                CardApplicantDetailsDTO::fromEntity
//        ).toList();
//
//        assertThat(cardApplicantDetailsDTOList).isNotNull();
//    }
//
//    @Test
//    @DisplayName("카드 신청 내역 리스트 가져오기")
//    void getCardApplicationTest() {
//
//        List<CardApplicantInfo> cardApplicantDetailsList = cardApplicantInfoRepository.findAll();
//
//        for (CardApplicantInfo applicantInfo : cardApplicantDetailsList) {
//            Long cardApplicantInfoId = applicantInfo.getCardApplicantInfoId();
//
//            Optional<CardApplicantInfo> cardApplicantInfo = cardApplicantInfoRepository.findByCardApplicantInfoId(cardApplicantInfoId);
//            assertThat(cardApplicantInfo.isPresent()).isEqualTo(true);
//        }
//    }
//
//    @Test
//    @DisplayName("카드 신청 생성하기")
//    void createCardApplicationTest() {
//        //given
//        CardApplicantDTO cardApplicantDTO = new CardApplicantDTO(1,1,true,false,true,"0000","0000",1,1,"9912311234567","사람이름", "영문이름", "12345", "배송주소", "배송상세주소", "mail@testmail.com", "01098765432", "자택주소", "자택상세주소");
//        LocalDate now = LocalDate.now();
//        CardDetail expectedCardDetail = new CardDetail(
//                cardApplicantDTO.paymentReceiptMethods(),
//                cardApplicantDTO.optionalTerms(),
//                LocalDate.of(now.getYear() + 5, now.getMonthValue(), 1),
//                "888",
//                cardApplicantDTO.cardPassword(),
//                cardApplicantDTO.isPostpaidTransport(),
//                cardApplicantDTO.isForeignBlocked(),
//                cardApplicantDTO.isAppCard(),
//                cardApplicantDTO.cardBrand(),
//                cardApplicantDTO.cardColor(),
//                "1234567898765432",
//                1L
//        );
//
//
//
//        CardApplicantInfo newCardApplicantInfo = CardApplicantInfo.builder()
//                .residenceNumber(cardApplicantDTO.residenceNumber())
//                .applicantName(cardApplicantDTO.applicantName())
//                .englishName(cardApplicantDTO.englishName())
//                .zipCode(cardApplicantDTO.zipCode())
//                .shippingAddress(cardApplicantDTO.shippingAddress())
//                .shippingDetailAddress(cardApplicantDTO.shippingDetailAddress())
//                .applicantEmail(cardApplicantDTO.applicantEmail())
//                .phoneNumber(cardApplicantDTO.phoneNumber())
//                .homeAddress(cardApplicantDTO.homeAddress())
//                .homeDetailAddress(cardApplicantDTO.homeDetailAddress())
//                .cardDetail(expectedCardDetail)
//                .build();
//
//        // then
//        assertThat(newCardApplicantInfo).isNotNull();
//        assertEquals(newCardApplicantInfo.getResidenceNumber(), cardApplicantDTO.residenceNumber());
//        assertEquals(newCardApplicantInfo.getApplicantName(), cardApplicantDTO.applicantName());
//        assertEquals(newCardApplicantInfo.getEnglishName(), cardApplicantDTO.englishName());
//        assertEquals(newCardApplicantInfo.getZipCode(), cardApplicantDTO.zipCode());
//        assertEquals(newCardApplicantInfo.getShippingAddress(), cardApplicantDTO.shippingAddress());
//        assertEquals(newCardApplicantInfo.getShippingDetailAddress(), cardApplicantDTO.shippingDetailAddress());
//        assertEquals(newCardApplicantInfo.getApplicantEmail(), cardApplicantDTO.applicantEmail());
//        assertEquals(newCardApplicantInfo.getPhoneNumber(), cardApplicantDTO.phoneNumber());
//        assertEquals(newCardApplicantInfo.getHomeAddress(), cardApplicantDTO.homeAddress());
//        assertEquals(newCardApplicantInfo.getHomeDetailAddress(), cardApplicantDTO.homeDetailAddress());
//
//    }
//
//}
