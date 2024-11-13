package com.quostomize.quostomize_be.api.hello.controller;

import com.quostomize.quostomize_be.api.hello.dto.StockInterestDto;
import com.quostomize.quostomize_be.domain.customizer.stock.service.StockInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StockInterestController {


    private final StockInterestService stockInterestService;

    // 조회 기능
    @GetMapping("/api/stocks/select")
    public ResponseEntity<List<StockInterestDto>> getStockWishList() {
        List<StockInterestDto> stockWishList = stockInterestService.getStockWishList();
        return ResponseEntity.ok(stockWishList);
    }

    // 삭제 기능
    @DeleteMapping("/api/stocks/select")
    public void deleteStock(@RequestParam int order){stockInterestService.deleteStock(order);}

    // 순위 변경 기능
    @PatchMapping("/api/stocks/select/change-rank")
    public void switchingStock(@RequestParam int currentOrder, @RequestParam int editOrder){
        stockInterestService.switchStock(currentOrder,editOrder);
}
}
