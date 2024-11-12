package com.quostomize.quostomize_be.api.stock.controller;

import com.quostomize.quostomize_be.domain.customizer.stock.service.StockAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockAccountController {

    private final StockAccountService stockAccountService;
    
    //TODO: 주식계좌 존재 유무 확인 후 존재하면 연동 가능한 데이터 끌어오기



    //TODO: 주식계좌 존재 유무 확인 후 존재하지 않으면 새로운 계좌 만들기 페이지 보여주기



    //TODO:
}
