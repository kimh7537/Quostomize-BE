package com.quostomize.quostomize_be.domain.customizer.stock;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "stocks_info")
public class StocksInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_information_id")
    private Long stockInformationId;

    @Column(name = "stock_code")
    private Integer stockCode;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name="stock_present_price")
    private Integer stockPresentPrice;

//    @Column(name = "stock_image")
//    private URI stockImage;

}
