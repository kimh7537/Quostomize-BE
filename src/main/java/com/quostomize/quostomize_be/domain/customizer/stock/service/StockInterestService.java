package com.quostomize.quostomize_be.domain.customizer.stock.service;

import com.quostomize.quostomize_be.api.stock.dto.CardBenefitResponse;
import com.quostomize.quostomize_be.api.stock.dto.StockRecommendResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.s3.S3Service;
import com.quostomize.quostomize_be.domain.customizer.benefit.entity.CardBenefit;
import com.quostomize.quostomize_be.domain.customizer.benefit.repository.CardBenefitRepository;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInformationRepository;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInterestRepository;
import com.quostomize.quostomize_be.api.stock.dto.StockInterestDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockInterestService {

    private final StockInterestRepository stockInterestRepository;
    private final S3Service s3Service;
    private final CardBenefitRepository cardBenefitRepository;
    private final StockInformationRepository stockInformationRepository;

    // 위시리스트를 조회합니다.
    @Transactional
    public List<StockInterestDto> getStockWishList() {
        // Step 1: Query로 Object[] 결과 받기
        List<Object[]> results = stockInterestRepository.findAllStockInterestDto();

        // Step 2: Object[] 결과를 StockInterestDto로 수동 변환
        List<StockInterestDto> allStockInterestDto = new ArrayList<>();
        for (Object[] result : results) {
            Integer priority = (Integer) result[0];
            String stockName = (String) result[1];
            Integer stockPresentPrice = (Integer) result[2];
            String stockImage = (String) result[3];

            // Step 3: DTO 객체 생성 후 리스트에 추가
            StockInterestDto dto = new StockInterestDto(priority, stockName, stockPresentPrice, stockImage);
            allStockInterestDto.add(dto);
        }
        List<StockInterestDto> collect = allStockInterestDto.stream()
                .map(stock -> {
                    // stockImage로 presigned URL을 생성
                    String preSignedUrl = s3Service.getPreSignedUrl(stock.stockImage());
                    // stock DTO를 수정하여 새로운 객체를 반환
                    return stock.withStockImage(preSignedUrl); // 새로운 객체 반환
                })
                .collect(Collectors.toList());
        return  collect;
    }

    // 위시리스트 중 해당 id에 해당하는 특정항목을 제거합니다.
    @Transactional
    public void deleteStock(int order){

        if(order == 1){
            stockInterestRepository.DeleteStockById(order);
            stockInterestRepository.switchStockDeleteOrder1();
        } else {
            stockInterestRepository.DeleteStockById(order);
            stockInterestRepository.switchStockDeleteOrder2();
        }
    }

    // 위시리스트 중 해당 id에 해당 특정항목을 특정 순서(order)로 변경합니다.
    @Transactional
    public void switchStock(int currentOrder, int editOrder){

        if(currentOrder == 3) {
            if(editOrder == 1){ // 현순위 3순위에서 1순위로 바꾸는 경우,
                stockInterestRepository.switchStock1(3);
            } else{ // 현순위 3순위 여기서 2순위로 바꾸는 경우
                stockInterestRepository.switchStock3(3);
            }
        } else if(currentOrder == 2){
            if(editOrder == 1){ //2순위를 1순위로
                stockInterestRepository.switchStock2(2);
            } else { // 2순위를 3순위로
                stockInterestRepository.switchStock4(2);
            }
        } else if(currentOrder == 1){
            if (editOrder == 2){ // 1순위를 2순의로 변경한다.
                stockInterestRepository.switchStock5(1);
            } else { // 1순위를 3순위로 변경한다.
                stockInterestRepository.switchStock6(1);
            }
        }
    }


    @Transactional
    public List<StockRecommendResponse> getCardBenefit(Long cardId, Boolean isRecommendByCardBenefit) {

        StockRecommendResponse stockRecommendResponse;
        List<StockRecommendResponse> recommendResponses = new ArrayList<>();
        // 카드 아이디 예외처리
        if (cardId == null) {
            throw new EntityNotFoundException("카드 혜택정보 혹은 결제 내역 정보가 존재하지 않습니다.");
        }

        // 카드 혜택 기반 주식 추천
        if (isRecommendByCardBenefit) {
            // 카드 혜택 조회
            List<CardBenefit> cardBenefits = cardBenefitRepository.findByCardDetail_CardSequenceId(cardId);
            // 카드 혜택 ResponseDto로 변환
            List<CardBenefitResponse> benefits = cardBenefits.stream()
                    .map(Benefit -> {
                        String benefitCategoryType = Benefit.getUpperCategory().getBenefitCategoryType();
                        String franchiseName = Benefit.getLowerCategory() != null
                                ? Benefit.getLowerCategory().getBenefitCategoryType()
                                : null; // 하위타입이 없으면 null을 넣는다
                        return new CardBenefitResponse(
                                Benefit.getIsActive(),
                                Benefit.getBenefitRate(),
                                benefitCategoryType,
                                franchiseName
                        );
                    }).collect(Collectors.toList());

            // default 이미지 링크
            String preSignedUrl = s3Service.getPreSignedUrl("image/quokka.png");
            // 상위분류 개수
            int upperNumber = 0;
            // 하위분류 개수
            int lowerNumber = 0;
            // 하위분류 이름
            HashMap<String, HashMap<String, Integer>> lowerName = new HashMap<String, HashMap<String, Integer>>();
            // 상위분류 이름
            HashMap<String, HashMap<String, Integer>> upperName = new HashMap<String, HashMap<String, Integer>>();

            for (CardBenefitResponse benefit : benefits) {
                if (benefit.isActive() == true) { // 혜택적립이 활성화 되어 있다면,
                    // 상위 분류- 하위 분류 이름, 적립률
                    HashMap<String, Integer> upper = new HashMap<String, Integer>();
                    // 하위 분류 - 하위 분류 이름, 적립률
                    HashMap<String, Integer> lower = new HashMap<String, Integer>();
                    if (benefit.franchiseName() == null) { // 하위분류가 null 일 때, <상위분류만 체크돼있을 떄>
                        upperNumber += 1; // 상위분류 개수 추가
                        upper.put(benefit.franchiseName(), benefit.benefitRate());
                        upperName.put(benefit.benefitCategoryType(), upper); // 상위분류 이름 및 적립률 추가
                    } else { // 하위분류가 체크되어있을때,
                        lowerNumber += 1; // 하위분류 개수 추가
                        lower.put(benefit.franchiseName(), benefit.benefitRate());
                        lowerName.put(benefit.benefitCategoryType(), lower); // 하위분류 이름 및 적립률 추가
                    }
                }
            }
            if (upperNumber == 5) { // 상위 분류만 5개가 있을 떄,
                // 혜택 적립률이 가장 높은 3가지의 테마에 대해서 주식 추천.
                // collect의 모든 entry를 순회하면서 내부의 HashMap을 정렬
                List<Map.Entry<String, HashMap<String, Integer>>> entryListUpper = upperName.entrySet().stream() .sorted((entry1, entry2) -> {
                            // 내부 HashMap에서 가장 큰 값을 찾고 내림차순으로 비교
                            int max1 = Collections.max(entry1.getValue().values());
                            int max2 = Collections.max(entry2.getValue().values());
                            return Integer.compare(max2, max1);
                        })
                        .collect(Collectors.toList());

                for (int i = 0; i < 3; i++) {
                    Map.Entry<String, HashMap<String,Integer>> entry = entryListUpper.get(i);
                    String category = entry.getKey();
                    if (category.equals("쇼핑")) { // 종목 추천 이때, 해당 종목은
                        StockInformation stocks = stockInformationRepository.findByStockCode(4170).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                        recommendResponses.add(stockRecommendResponse);
                    } else if (category.equals("생활")) {
                        StockInformation stocks = stockInformationRepository.findByStockCode(17670).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                        recommendResponses.add(stockRecommendResponse);
                    } else if (category.equals("푸드")) {
                        StockInformation stocks = stockInformationRepository.findByStockCode(78930).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                        recommendResponses.add(stockRecommendResponse);
                    } else if (category.equals("여행")) {
                        StockInformation stocks = stockInformationRepository.findByStockCode(39130).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                        recommendResponses.add(stockRecommendResponse);
                    } else if (category.equals("문화")) {
                        StockInformation stocks = stockInformationRepository.findByStockCode(79160).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                        recommendResponses.add(stockRecommendResponse);
                    }
                }
            } else if (lowerNumber == 5) { // 하위 분류만 5개가 있을 떄,
                // 혜택 적립률이 가장 높은 3가지의 하위분류에 대해서 주식 추천.
                // 적립률 순서대로 배열 내 인덱스를 정렬진행
                List<Map.Entry<String, HashMap<String, Integer>>> entryListLower = lowerName.entrySet().stream() .sorted((entry1, entry2) -> {
                            // 내부 HashMap에서 가장 큰 값을 찾고 내림차순으로 비교
                            int max1 = Collections.max(entry1.getValue().values());
                            int max2 = Collections.max(entry2.getValue().values());
                            return Integer.compare(max2, max1);
                        })
                        .collect(Collectors.toList());

                for (int i = 0; i < 3; i++) {
                    Map.Entry<String, HashMap<String,Integer>> example = entryListLower.get(i);
                    String category = example.getKey();
                    HashMap<String, Integer> sortedexample = example.getValue();

                    for(Map.Entry<String, Integer> innerEntry : sortedexample.entrySet()){
                        if (category.equals("쇼핑")) { // 종목 추천 이때, 해당 종목은
                            if (innerEntry.getKey().equals("백화점")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(69960).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("온라인쇼핑")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(57050).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("마트")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(4170).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            }
                        } else if (category.equals("생활")) {
                            if (innerEntry.getKey().equals("주유소")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(1510).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("통신")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(17670).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("대중교통")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(78930).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            }
                        } else if (category.equals("푸드")) {
                            if (innerEntry.getKey().equals("편의점")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(78930).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("커피")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(30200).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("배달")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(357780).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            }
                        } else if (category.equals("여행")) {
                            if (innerEntry.getKey().equals("투어")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(39130).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("차량")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(264900).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("숙소")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(10440).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            }
                        } else if (category.equals("문화")) {
                            if (innerEntry.getKey().equals("OTT")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(153450).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("영화관")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(79160).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (innerEntry.getKey().equals("도서")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(418470).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            }
                        }
                    }


                }
            } else { // 상위 하위 분류가 동시에 존재할 떄,
                // 혜택 적립률이 가장 높은 3가지를 선정하여, 하위분류 혹은 상위분류 주식 추천

                // 하위분류에 대해서 적립률이 높은순서대로 정렬한 배열 객체 생성
                List<Map.Entry<String, HashMap<String, Integer>>> entryListLower = lowerName.entrySet().stream() .sorted((entry1, entry2) -> {
                            // 내부 HashMap에서 가장 큰 값을 찾고 내림차순으로 비교
                            int max1 = Collections.max(entry1.getValue().values());
                            int max2 = Collections.max(entry2.getValue().values());
                            return Integer.compare(max2, max1);
                        })
                        .collect(Collectors.toList());

                // 상위 분류에 대해서 적립률이 높은 순서대로 정렬한 배열 객체 생성
                    List<Map.Entry<String, HashMap<String, Integer>>> entryListUpper = upperName.entrySet().stream() .sorted((entry1, entry2) -> {
                                // 내부 HashMap에서 가장 큰 값을 찾고 내림차순으로 비교
                                int max1 = Collections.max(entry1.getValue().values());
                                int max2 = Collections.max(entry2.getValue().values());
                                return Integer.compare(max2, max1);
                            })
                            .collect(Collectors.toList());
                int lowerIndex = 0; // 하위 인덱스 진행 사이클 수
                int upperIndex = 0; // 상위 인덱스 진행 사이클 수
                for (int i = 0; i < 3; i++) {
                    if(lowerIndex < entryListLower.size() || upperIndex < entryListUpper.size()) {


                        if (entryListLower.get(lowerIndex).getValue().entrySet().stream().findFirst().get().getValue() > entryListUpper.get(upperIndex).getValue().entrySet().stream().findFirst().get().getValue()) { // 혜택률이 하위 분류가 더 큰 경우
                            // 하위 인덱스를 진행시
                            Map.Entry<String, HashMap<String, Integer>> example = entryListLower.get(i);
                            String category = example.getKey();
                            HashMap<String, Integer> sortedexample = example.getValue();
                            lowerIndex += 1; // 하위 인덱스 진행시 사이클 추가

                            for (Map.Entry<String, Integer> innerEntry : sortedexample.entrySet()) {
                                if (category.equals("쇼핑")) { // 종목 추천 이때, 해당 종목은
                                    if (innerEntry.getKey().equals("백화점")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(69960).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("온라인쇼핑")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(57050).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("마트")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(4170).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    }
                                } else if (category.equals("생활")) {
                                    if (innerEntry.getKey().equals("주유소")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(1510).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("통신")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(17670).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("대중교통")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(78930).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    }
                                } else if (category.equals("푸드")) {
                                    if (innerEntry.getKey().equals("편의점")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(78930).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("커피")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(30200).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("배달")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(357780).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    }
                                } else if (category.equals("여행")) {
                                    if (innerEntry.getKey().equals("투어")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(39130).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("차량")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(264900).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("숙소")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(10440).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    }
                                } else if (category.equals("문화")) {
                                    if (innerEntry.getKey().equals("OTT")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(153450).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("영화관")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(79160).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    } else if (innerEntry.getKey().equals("도서")) {
                                        StockInformation stocks = stockInformationRepository.findByStockCode(418470).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                        stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                        recommendResponses.add(stockRecommendResponse);
                                    }
                                }
                            }
                        } else { // 상위 인덱스를 진행 시
                            Map.Entry<String, HashMap<String, Integer>> entry = entryListUpper.get(i);
                            String category = entry.getKey();
                            upperIndex += 1; // 상위 인덱스를 추가

                            if (category.equals("쇼핑")) { // 종목 추천 이때, 해당 종목은
                                StockInformation stocks = stockInformationRepository.findByStockCode(4170).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (category.equals("생활")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(17670).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (category.equals("푸드")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(78930).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (category.equals("여행")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(39130).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            } else if (category.equals("문화")) {
                                StockInformation stocks = stockInformationRepository.findByStockCode(79160).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("이거 에바인데요")));
                                stockRecommendResponse = new StockRecommendResponse(stocks.getStockName(),stocks.getStockPresentPrice(),preSignedUrl);
                                recommendResponses.add(stockRecommendResponse);
                            }
                        }
                    }
                }
            }
        }
        else{ // 결제 내역 기반 주식 추천
        }
        // 추천종목 리턴
        return recommendResponses;
    }
}

