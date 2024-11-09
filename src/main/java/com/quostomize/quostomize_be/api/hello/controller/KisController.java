package com.quostomize.quostomize_be.api.hello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KisController {

    private final KisService kisService;
    private final StockBalanceService stockBalanceService;


    @GetMapping("/volume-rank")
    public List<ResponseOutputDTO> getVolumeRank() {
        return kisService.getVolumeRank();
    }

    @GetMapping("/stock-balance")
    public ApiResponseDTO getStockBalance() {
        return stockBalanceService.getVolumeRank();
    }
}