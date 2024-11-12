package com.quostomize.quostomize_be.domain.customizer.stock.service;

import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockAccountService {

    private final StockAccountRepository stockAccountRepository;


}
