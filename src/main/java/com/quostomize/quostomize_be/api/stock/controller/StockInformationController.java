package com.quostomize.quostomize_be.api.stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.stock.dto.StockInformationResponse;
import com.quostomize.quostomize_be.api.stock.dto.StockSearchResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.stock.elasticSearch.StockInformationSearchService;
import com.quostomize.quostomize_be.domain.customizer.stock.service.StockInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/stocks")
@RequiredArgsConstructor
@Tag(name = "주식 정보 API", description = "내 게좌에 있는 주식 정보를 가져오고 검색할 수 있음")
public class StockInformationController {

    private final StockInformationService stockInformationService;
    private final StockInformationSearchService stockInformationSearchService;

    @GetMapping("/lists/{stockAccountId}")
    @Operation(summary = "계좌 정보로 보유 주식 가져오기", description = "연결된 게좌 정보를 OpenAPI를 활용해 보유한 주식 정보를 모두 가져옴")
    public ResponseEntity<StockInformationResponse> getStockBalance(@PathVariable long stockAccountId){
        StockInformationResponse response =  stockInformationService.showStockInformation(stockAccountId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/search")
    @Operation(summary = "검색해서 주식 종목 가져오기", description = "주식 정보를 검색해서 가져올 수 있음")
    public ResponseEntity<ResponseDTO<List<StockSearchResponse>>> searchPost(@RequestParam(value = "keyword") String keyword) {

        // Service에서 검색된 Document를 Response DTO로 변환
        List<StockSearchResponse> responses = stockInformationSearchService.search(keyword)
                .stream()
                .map(document -> {
                    return new StockSearchResponse(
                            document.getStockInformationId(),
                            document.getStockCode(),
                            document.getStockName(),
                            document.getStockPresentPrice(),
                            document.getStockImage()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO<>(responses));
    }

}
