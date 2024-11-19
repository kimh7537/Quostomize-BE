package com.quostomize.quostomize_be.domain.customizer.stock.elasticSearch;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@Mapping(mappingPath = "static/elastic-mapping.json")
@Setting(settingPath = "static/elastic-token.json")
@Document(indexName = "stock_informations")
public class StockInformationDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Integer)
    private Integer stockCode;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer", searchAnalyzer = "nori_analyzer")
    private String stockName;

    @Field(type = FieldType.Integer)
    private Integer stockPresentPrice;

    @Field(type = FieldType.Text, index = false)
    private String stockImage;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private String createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private String modifiedAt;

}
