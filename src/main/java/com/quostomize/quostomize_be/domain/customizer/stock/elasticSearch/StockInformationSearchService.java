package com.quostomize.quostomize_be.domain.customizer.stock.elasticSearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInformationSearchService {

    private final StockInformationDocumentRepository stockInformationRepository;

    public StockInformationDocument createStockInformationDocument(StockInformationDocument stockInformationDocument) {
        return stockInformationRepository.save(stockInformationDocument);
    }

    public List<StockInformationDocument> searchStockInformationDocument(String keyword) {
        List<StockInformationDocument> stockInformationDocuments = stockInformationRepository.findByStockName(keyword);
        return stockInformationDocuments;
    }
}
