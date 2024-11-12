package com.quostomize.quostomize_be.domain.customizer.stock.service;

import com.quostomize.quostomize_be.api.stock.dto.StockAccountResponse;
import com.quostomize.quostomize_be.api.stock.dto.StockAccountStatusResponse;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockAccount;
import com.quostomize.quostomize_be.domain.customizer.stock.enums.PageType;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockAccountService {

    private final StockAccountRepository stockAccountRepository;

    public StockAccountStatusResponse getAllStockAccountsByCustomerId(Long customerId) {
        List<StockAccount> accounts = stockAccountRepository.findAllByCustomer_CustomerId(customerId);
        if(accounts.isEmpty()) {
            return emptyAccountResponse();
        }

        Optional<StockAccount> activeAccount = accounts.stream()
                .filter(StockAccount::getStockAccountActive)
                .findFirst();

        return activeAccount
                .map(this::createActiveAccountResponse) // 활성화된 계좌가 있는 경우, 해당 계좌를 파라미터로 전달
                .orElseGet(() -> createInactiveAccountResponse(accounts)); // 모든 계좌가 비활성화된 경우
    }


    private StockAccountStatusResponse emptyAccountResponse() {
        return new StockAccountStatusResponse(PageType.CREATE_STOCK, null);
    }



    private StockAccountStatusResponse createActiveAccountResponse(StockAccount account) {
        return new StockAccountStatusResponse(
                PageType.MY_STOCK,
                Collections.singletonList(StockAccountResponse.from(account))
        );
    }


    private StockAccountStatusResponse createInactiveAccountResponse(List<StockAccount> accounts) {
        List<StockAccountResponse> accountResponses = accounts.stream()
                .map(StockAccountResponse::from)
                .collect(Collectors.toList());
        return new StockAccountStatusResponse(PageType.LINK_ACCOUNT, accountResponses);
    }
}
