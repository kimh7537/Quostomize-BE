package com.quostomize.quostomize_be.api.hello.service;

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
    public void deleteStock(Long id){
        stockInterestRepository.DeleteStockById(id);
    }

    // 위시리스트 중 해당 id에 해당 특정항목을 특정 순서(order)로 변경합니다.
    public void switchStock(int order){
        if(order == 3) {
            stockInterestRepository.switchStock1(3, 3);
        } else{
            stockInterestRepository.switchStock2(2);
            stockInterestRepository.switchStock3(4);
        }

    }
}
