package com.quostomize.quostomize_be.domain.customizer.stock.common;

import com.quostomize.quostomize_be.api.stock.dto.StockRecommendResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInformationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LowerClassfication {
    public void addRecommendList(ComputeEntry computeEntry
            , HashMap<String, HashMap<String, Integer>> lowerName
            , StockInformationRepository stockInformationRepository
            , String preSignedUrl
            , List<StockRecommendResponse> recommendResponses ) {

        List<Map.Entry<String, HashMap<String, Integer>>> entryListLower = computeEntry.getListEntry(lowerName);

        for (int i = 0; i < 3; i++) {
            Map.Entry<String, HashMap<String, Integer>> entry = entryListLower.get(i); // 제일 높은 적립률 순으로 불러오기 진행
            String category = entry.getKey();
            HashMap<String, Integer> sortedexample = entry.getValue();
            StockRecommendResponse stockRecommendResponse;

                for (Map.Entry<String, Integer> innerEntry : sortedexample.entrySet()) {
                    switch (category) {
                        case "쇼핑":
                            switch (innerEntry.getKey()) {
                                case "백화점": addStockToResponse(69960, preSignedUrl,stockInformationRepository,recommendResponses); break;
                                case "온라인쇼핑": addStockToResponse(57050, preSignedUrl,stockInformationRepository,recommendResponses); break;
                                case "마트": addStockToResponse(4170, preSignedUrl,stockInformationRepository,recommendResponses); break;
                                default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                            } break;

                        case "생활":
                            switch (innerEntry.getKey()) {
                                case "주유소": addStockToResponse(1510, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "통신": addStockToResponse(17670, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "대중교통": addStockToResponse(78930, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                            } break;

                        case "푸드":
                            switch (innerEntry.getKey()) {
                                case "편의점": addStockToResponse(78930, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "커피": addStockToResponse(30200, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "배달": addStockToResponse(357780, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                            } break;

                        case "여행":
                            switch (innerEntry.getKey()) {
                                case "투어": addStockToResponse(39130, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "차량": addStockToResponse(264900, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "숙소": addStockToResponse(10440, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                            } break;

                        case "문화":
                            switch (innerEntry.getKey()) {
                                case "OTT": addStockToResponse(153450, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "영화관": addStockToResponse(79160, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                case "도서": addStockToResponse(418470, preSignedUrl,stockInformationRepository,recommendResponses);break;
                                default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                            } break;

                        default:
                            throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 값이 유효하지 않습니다."));
                    }
                }
            }
        }
    // 공통 로직을 별도의 메소드로 추출
    private void addStockToResponse(int stockCode, String preSignedUrl, StockInformationRepository stockInformationRepository, List<StockRecommendResponse> recommendResponses) {
        StockInformation stocks = stockInformationRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다.")));
        recommendResponses.add(new StockRecommendResponse(stocks.getStockName(), stocks.getStockPresentPrice(), preSignedUrl));
    }
}

