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

    private final StockInterestRepository stockInterestRepository;
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
}
