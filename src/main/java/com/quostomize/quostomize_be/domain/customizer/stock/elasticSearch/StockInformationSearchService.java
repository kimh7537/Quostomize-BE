package com.quostomize.quostomize_be.domain.customizer.stock.elasticSearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockInformationSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<StockInformationDocument> search(String stockName){
        Query query = QueryBuilders.match(queryBuilder -> queryBuilder.field("stock_name").query(stockName));
        NativeQuery nativeQuery = NativeQuery.builder().withQuery(query).build();
        SearchHits<StockInformationDocument> result = elasticsearchOperations.search(nativeQuery, StockInformationDocument.class);
        return result
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

}
