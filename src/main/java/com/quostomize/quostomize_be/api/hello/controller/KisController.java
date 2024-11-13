package com.quostomize.quostomize_be.api.hello.controller;

import com.quostomize.quostomize_be.api.stock.dto.StockInformationResponse;
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

    @GetMapping("/stock-balance/good")
    public Response get() {
        return stockBalanceService.getVolumeRank();
    }
}