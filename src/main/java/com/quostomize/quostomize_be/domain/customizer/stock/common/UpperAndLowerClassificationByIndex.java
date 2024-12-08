package com.quostomize.quostomize_be.domain.customizer.stock.common;

import com.quostomize.quostomize_be.api.stock.dto.StockRecommendResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.s3.S3Service;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInformationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpperAndLowerClassificationByIndex {

    public void getUpperList(Map.Entry<String, HashMap<String, Integer>> entry, StockInformationRepository stockInformationRepository, List<StockRecommendResponse> recommendResponses, S3Service s3Service){
        String category = entry.getKey();
        switch ( category ) {
            case "쇼핑": addStockToResponse(4170,stockInformationRepository,recommendResponses,s3Service); break;
            case "생활": addStockToResponse(17670,stockInformationRepository,recommendResponses,s3Service); break;
            case "푸드": addStockToResponse(78930,stockInformationRepository,recommendResponses,s3Service); break;
            case "여행": addStockToResponse(39130,stockInformationRepository,recommendResponses,s3Service); break;
            case"문화": addStockToResponse(79160,stockInformationRepository,recommendResponses,s3Service); break;
            default: new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("유효하지 않은 카테고리 입니다.")); break;
        }
    }

    public void getLowerList(Map.Entry<String, HashMap<String, Integer>> entry, StockInformationRepository stockInformationRepository, List<StockRecommendResponse> recommendResponses, S3Service s3Service){
        String category = entry.getKey();
        HashMap<String, Integer> sortedexample = entry.getValue();

        for (Map.Entry<String, Integer> innerEntry : sortedexample.entrySet()) {
            switch (category) {
                case "쇼핑":
                    switch (innerEntry.getKey()) {
                        case "백화점": addStockToResponse(69960,stockInformationRepository,recommendResponses,s3Service); break;
                        case "온라인쇼핑": addStockToResponse(57050,stockInformationRepository,recommendResponses,s3Service); break;
                        case "마트": addStockToResponse(4170,stockInformationRepository,recommendResponses,s3Service); break;
                        default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                    } break;

                case "생활":
                    switch (innerEntry.getKey()) {
                        case "주유소": addStockToResponse(1510,stockInformationRepository,recommendResponses,s3Service);break;
                        case "통신": addStockToResponse(17670,stockInformationRepository,recommendResponses,s3Service);break;
                        case "대중교통": addStockToResponse(78930,stockInformationRepository,recommendResponses,s3Service);break;
                        default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                    } break;

                case "푸드":
                    switch (innerEntry.getKey()) {
                        case "편의점": addStockToResponse(78930,stockInformationRepository,recommendResponses,s3Service);break;
                        case "커피": addStockToResponse(30200,stockInformationRepository,recommendResponses,s3Service);break;
                        case "배달": addStockToResponse(357780,stockInformationRepository,recommendResponses,s3Service);break;
                        default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                    } break;

                case "여행":
                    switch (innerEntry.getKey()) {
                        case "투어": addStockToResponse(39130,stockInformationRepository,recommendResponses,s3Service);break;
                        case "차량": addStockToResponse(264900,stockInformationRepository,recommendResponses,s3Service);break;
                        case "숙소": addStockToResponse(10440,stockInformationRepository,recommendResponses,s3Service);break;
                        default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                    } break;

                case "문화":
                    switch (innerEntry.getKey()) {
                        case "OTT": addStockToResponse(153450,stockInformationRepository,recommendResponses,s3Service);break;
                        case "영화관": addStockToResponse(79160,stockInformationRepository,recommendResponses,s3Service);break;
                        case "도서": addStockToResponse(418470,stockInformationRepository,recommendResponses,s3Service);break;
                        default: throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다."));
                    } break;

                default:
                    throw new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 값이 유효하지 않습니다."));
            }
        }
    }
    // 공통 로직을 별도의 메소드로 추출
    private void addStockToResponse(int stockCode, StockInformationRepository stockInformationRepository, List<StockRecommendResponse> recommendResponses, S3Service s3Service) {
        StockInformation stocks = stockInformationRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다.")));
        recommendResponses.add(new StockRecommendResponse(stocks.getStockName(), stocks.getStockPresentPrice(), s3Service.getPreSignedUrl(stocks.getStockImage())));
    }
}
