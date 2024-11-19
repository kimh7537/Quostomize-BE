package com.quostomize.quostomize_be.domain.customizer.stock.elasticSearch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockInformationSearchService {

    private final StockInformationDocumentRepository stockInformationRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    public StockInformationDocument createStockInformationDocument(StockInformationDocument stockInformationDocument) {
        return stockInformationRepository.save(stockInformationDocument);
    }

    public List<StockInformationDocument> searchStockInformationDocument(String keyword) {
        List<StockInformationDocument> stockInformationDocuments = stockInformationRepository.findByStockName(keyword);
        return stockInformationDocuments;
    }

}
