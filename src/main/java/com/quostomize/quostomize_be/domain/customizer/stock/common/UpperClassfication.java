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

public class UpperClassfication {

    public void addRecommendList(ComputeWithLowerAndUpper computeWithLowerAndUpper
            , HashMap<String, HashMap<String, Integer>> upperName
            , StockInformationRepository stockInformationRepository
            , List<StockRecommendResponse> recommendResponses
            , S3Service s3Service){

        List<Map.Entry<String, HashMap<String, Integer>>> entryListUpper =computeWithLowerAndUpper.getEntryListUpper() ;

        for (int i = 0; i < 3; i++) {
            Map.Entry<String, HashMap<String, Integer>> entry = entryListUpper.get(i); // 제일 높은 적립률 순으로 불러오기 진행
            String category = entry.getKey();

            switch ( category ) {
                case "쇼핑": addStockToResponse(4170,stockInformationRepository,recommendResponses, s3Service); break;
                case "생활": addStockToResponse(17670,stockInformationRepository,recommendResponses, s3Service); break;
                case "푸드": addStockToResponse(78930,stockInformationRepository,recommendResponses, s3Service); break;
                case "여행": addStockToResponse(39130,stockInformationRepository,recommendResponses, s3Service); break;
                case"문화": addStockToResponse(79160,stockInformationRepository,recommendResponses, s3Service); break;
                default: new AppException(ErrorCode.ENTITY_NOT_FOUND,new Throwable("유효하지 않은 카테고리 입니다.")); break;
            }
        }
    }
    // 공통 로직을 별도의 메소드로 추출
    private void addStockToResponse(int stockCode, StockInformationRepository stockInformationRepository, List<StockRecommendResponse> recommendResponses, S3Service s3Service) {
        StockInformation stocks = stockInformationRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, new Throwable("상위분류 혹은 하위분류값이 유효하지 않습니다.")));
        recommendResponses.add(new StockRecommendResponse(stocks.getStockName(), stocks.getStockPresentPrice(),s3Service.getPreSignedUrl(stocks.getStockImage()) ));
    }
}
