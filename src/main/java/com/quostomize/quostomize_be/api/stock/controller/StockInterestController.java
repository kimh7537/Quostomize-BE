package com.quostomize.quostomize_be.api.stock.controller;

import com.quostomize.quostomize_be.api.stock.dto.StockInterestDto;
import com.quostomize.quostomize_be.api.stock.dto.StockInterestRequestDto;
import com.quostomize.quostomize_be.api.stock.dto.StockRecommendResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.stock.service.StockInterestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockInterestController {


    private final StockInterestService stockInterestService;

    // 조회 기능
    @GetMapping("/select")
    @Operation(summary = "위시리스토 조회",description = "현재 적용 되어있는 위시리스트를 조회합니다.")
    public ResponseEntity<ResponseDTO<List<StockInterestDto>>> getStockWishList(@RequestParam Long cardId) {
        List<StockInterestDto> stockWishList = stockInterestService.getStockWishList(cardId);
        return ResponseEntity.ok(new ResponseDTO<>(stockWishList));
    }

    // 삭제 기능
    @DeleteMapping("/select")
    @Operation(summary = "위시리스토 삭제",description = "선택한 위시항목에 대해서 삭제합니다.")
    public ResponseEntity<Void> deleteStock(@RequestParam int order){
        stockInterestService.deleteStock(order);
        return ResponseEntity.noContent().build();
    }

    // 순위 변경 기능
    @PatchMapping("/select/change-rank")
    @Operation(summary = "위시리스토 순위변경",description = "선택 되어있는 위시리스트의 순위(priority)를 변경 합니다.")
    public ResponseEntity<Void> switchingStock(@RequestBody List<StockInterestRequestDto> dtos){
        stockInterestService.switchStock(dtos);
        return  ResponseEntity.noContent().build();
    }
    //선택한 혜택 기반 그리고 결제 내역 기반에 따른 추천로직
    @GetMapping("/recommendations")
    @Operation(summary = "종목추천",description = "선택한 혜택 그리고 결제 내역 기반으로 종목을 추천합니다.")
    public ResponseEntity<ResponseDTO<List<StockRecommendResponse>>> getRecommandStocks(@RequestParam Long cardId, @RequestParam boolean isRecommendByCardBenefit){
        List<StockRecommendResponse> cardBenefit = stockInterestService.getCardBenefit(cardId,isRecommendByCardBenefit);
        return ResponseEntity.ok(new ResponseDTO<>(cardBenefit));
    }
}
