package com.quostomize.quostomize_be.domain.customizer.stock.elasticSearch;

import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//@Setting(settingPath = "es-config/es-analyzer.json")
@Document(indexName = "stock_informations")
public class StockInformationDocument {

    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long stockInformationId;

    @Field(name = "stock_code", type = FieldType.Integer)
    private Integer stockCode;

    @Field(name = "stock_name", type = FieldType.Text, analyzer = "my_nori_analyzer", searchAnalyzer = "my_nori_analyzer")
    private String stockName;

    @Field(name = "stock_present_price", type = FieldType.Integer)
    private Integer stockPresentPrice;

    @Field(name = "stock_image", type = FieldType.Text, index = false)
    private String stockImage;

    @Field(name = "created_at", type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_fraction)
    private OffsetDateTime modifiedAt;


    public StockInformation toEntity() {
        return StockInformation.builder()
                .stockCode(this.stockCode)
                .stockName(this.stockName)
                .stockPresentPrice(this.stockPresentPrice)
                .stockImage(this.stockImage)
                .build();
    }
}
