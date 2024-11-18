package com.quostomize.quostomize_be.domain.customizer.stock.elasticSearch;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@Document(indexName = "stockInformation")
@Mapping(mappingPath = "static/elastic-mapping.json")
//@Setting(settingPath = "static/elastic-token.json")
public class StockInformationDocument {

    @Id
    @Field(type = FieldType.Keyword)  // Elasticsearch에서 ID는 일반적으로 Keyword 타입
    private String id;

    @Field(type = FieldType.Integer)
    private Integer stockCode;

    @Field(type = FieldType.Text)
    private String stockName;

    @Field(type = FieldType.Integer)
    private Integer stockPresentPrice;

    @Field(type = FieldType.Text)
    private String stockImage;
}
