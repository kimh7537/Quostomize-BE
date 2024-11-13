package com.quostomize.quostomize_be.api.stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.stock.dto.StockInformationResponse;
import com.quostomize.quostomize_be.domain.customizer.stock.service.StockInformationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockInformationController {

    private final StockInformationService stockInformationService;

    @PostMapping("/access")
    public ResponseEntity<String> getOpenAPIAccessToken(){
        String accessToken = stockInformationService.getOpenAPIAccessToken();
        return ResponseEntity.status(HttpStatus.OK).body(accessToken);
    }

    @GetMapping("/lists/{stockAccountId}")
    @Operation(summary = "계좌 정보로 보유 주식 가져오기", description = "연결된 게좌 정보를 OpenAPI를 활용해 보유한 주식 정보를 모두 가져옴")
    public ResponseEntity<StockInformationResponse> getStockBalance(@PathVariable long stockAccountId){
        StockInformationResponse response =  stockInformationService.showStockInformation(stockAccountId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
