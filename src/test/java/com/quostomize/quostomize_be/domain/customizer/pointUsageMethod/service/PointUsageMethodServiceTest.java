//package com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.service;
//
//import com.quostomize.quostomize_be.common.error.ErrorCode;
//import com.quostomize.quostomize_be.common.error.exception.AppException;
//import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
//import com.quostomize.quostomize_be.domain.customizer.card.repository.CardDetailRepository;
//import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.entity.PointUsageMethod;
//import com.quostomize.quostomize_be.domain.customizer.pointUsageMethod.repository.PointUsageMethodRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//@Transactional
//@ActiveProfiles("Test")
//class PointUsageMethodServiceTest {
//
//    @Autowired
//    private PointUsageMethodService pointUsageMethodService;
//
//    @Autowired
//    private PointUsageMethodRepository repository;
//
//    @Autowired
//    private CardDetailRepository cardDetailRepository;
//
//    @BeforeEach
//    void setUp() {
//        repository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("옵션 활성화 수 제한(최소 1개)")
//    void test1() {
//        // given
//        Long cardSequenceId = 1L;
//
//        // cardDetail 객체 조회 또는 생성
//        CardDetail cardDetail = cardDetailRepository.findById(cardSequenceId)
//                .orElseGet(() -> {
//                    CardDetail newCardDetail = CardDetail.builder()
//                            .cardSequenceId(cardSequenceId)
//                            .build();
//                    return cardDetailRepository.save(newCardDetail);
//                });
//
//        // PointUsageMethod 객체 생성 (cardDetail을 올바르게 설정)
//        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
//                .cardDetail(cardDetail)
//                .isLotto(false)
//                .isPayback(false)
//                .isPieceStock(false)
//                .build();
//
//        // DB에 PointUsageMethod 저장
//        repository.save(pointUsageMethod);
//
//        // 최소 옵션 활성화 예외 테스트
//        AppException min = assertThrows(AppException.class, () ->
//                pointUsageMethodService.togglePointUsage(cardSequenceId, "lotto", false));
//        assertThat(min.getErrorCode()).isEqualTo(ErrorCode.MINIMUM_ONE_OPTION_REQUIRED);
//    }
//
//    @Test
//    @DisplayName("페이백, 조각투각 옵션을 동시에 활성화 시 예외 발생")
//    void test2() {
//
//
//        // given
//        Long cardSequenceId = 1L;
//
//        // cardDetail 객체 조회 또는 생성
//        CardDetail cardDetail = cardDetailRepository.findById(cardSequenceId)
//                .orElseGet(() -> {
//                    CardDetail newCardDetail = CardDetail.builder()
//                            .cardSequenceId(cardSequenceId)
//                            .build();
//                    return cardDetailRepository.save(newCardDetail);
//                });
//
//        // PointUsageMethod 객체 생성 (cardDetail을 올바르게 설정)
//        PointUsageMethod pointUsageMethod = PointUsageMethod.builder()
//                .cardDetail(cardDetail)
//                .isLotto(false)
//                .isPayback(false)
//                .isPieceStock(false)
//                .build();
//
//        // DB에 PointUsageMethod 저장
//        repository.save(pointUsageMethod);
//
//        // payback 활성화
//        pointUsageMethodService.togglePointUsage(cardSequenceId, "payback", true);
//
//        // when, then - "piecestock 활성화 시도
//        AppException exception = assertThrows(AppException.class, () ->
//                pointUsageMethodService.togglePointUsage(cardSequenceId, "piecestock", true));
//
//        // 예외
//        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PAYBACK_AND_PIECESTOCK_CONFLICT);
//    }
//
//}