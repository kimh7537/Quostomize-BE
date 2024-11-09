package com.quostomize.quostomize_be.domain.customizer.stock.service;

import com.quostomize.quostomize_be.common.s3.S3Service;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInterestRepository;
import com.quostomize.quostomize_be.api.hello.dto.StockInterestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockInterestService {

    @Autowired
    private final StockInterestRepository stockInterestRepository;

    @Autowired
    private final S3Service s3Service;

    // 위시리스트를 조회합니다.
    public List<StockInterestDto> getStockWishList() {
        List<StockInterestDto> allStockInterestDto = stockInterestRepository.findAllStockInterestDto();
        List<StockInterestDto> collect = allStockInterestDto.stream()
                .map(stock -> {
                    // stockImage로 presigned URL을 생성
                    String preSignedUrl = s3Service.getPreSignedUrl(stock.stockImage());
                    // stock DTO를 수정하여 새로운 객체를 반환
                    return stock.withStockImage(preSignedUrl); // 새로운 객체 반환
                })
                .collect(Collectors.toList());
        return collect;
    }

    // 위시리스트 중 해당 id에 해당하는 특정항목을 제거합니다.
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
    public void switchStock(int order){
        if(order == 3) {
            // 3순위를 1순위로 변경, 1순위 2순위로 변경, 2순위를 3순위로 변경
            stockInterestRepository.switchStock1(3);
        } else{
            // 2순위를 1순위로 변경, 1순위를 2순위로 변경
            stockInterestRepository.switchStock2(2);
        }

    }
}
