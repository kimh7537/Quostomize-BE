package com.quostomize.quostomize_be.api.hello.controller;

import com.quostomize.quostomize_be.api.hello.dto.StockInterestDto;
import com.quostomize.quostomize_be.api.hello.service.StockInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StockInterestController {


    private final StockInterestService stockInterestService;

    @GetMapping("/api/stocks/select")
    public ResponseEntity<List<StockInterestDto>> getStockWishList() {
        List<StockInterestDto> stockWishList = stockInterestService.getStockWishList();
        return ResponseEntity.ok(stockWishList);
    }

    @DeleteMapping("/api/stocks/select")
    public void deleteStock(@RequestParam Long id){
        stockInterestService.deleteStock(id);
    }

    @PatchMapping("/api/stocks/select/change-rank")
    public void switchingStock(@RequestParam int order){
        stockInterestService.switchStock(order);
}
}
