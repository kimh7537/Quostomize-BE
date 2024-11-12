package com.quostomize.quostomize_be.api.stock.controller;

import com.quostomize.quostomize_be.domain.customizer.stock.service.StockAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockAccountController {

    private final StockAccountService stockAccountService;
}
