package com.quostomize.quostomize_be.api.stock.controller;

import com.quostomize.quostomize_be.api.stock.dto.StockAccountResponse;
import com.quostomize.quostomize_be.api.stock.dto.StockAccountStatusResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.stock.service.StockAccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/stocks/accounts")
@RequiredArgsConstructor
@Slf4j
public class StockAccountController {

    private final StockAccountService stockAccountService;

    @GetMapping("/{customerId}")
    @Operation(summary = "고객 ID로 주식 계좌 상태 확인", description = "고객의 주식 계좌가 활성화 상태인지, 비활성화된 상태인지 또는 존재하지 않는지 확인합니다.")
    public ResponseEntity<StockAccountStatusResponse> getStockAccount(@PathVariable Long customerId) {
        StockAccountStatusResponse response = stockAccountService.getAllStockAccountsByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }






    //TODO:
}
