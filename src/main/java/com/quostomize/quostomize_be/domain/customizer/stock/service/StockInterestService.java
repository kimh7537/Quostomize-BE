package com.quostomize.quostomize_be.domain.customizer.stock.service;

import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInterestRepository;
import com.quostomize.quostomize_be.api.hello.dto.StockInterestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInterestService {


    private final StockInterestRepository stockInterestRepository;

    // 위시리스트를 조회합니다.
    public List<StockInterestDto> getStockWishList() {
        List<StockInterestDto> allStockInterestDto = stockInterestRepository.findAllStockInterestDto();
        return allStockInterestDto;
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
