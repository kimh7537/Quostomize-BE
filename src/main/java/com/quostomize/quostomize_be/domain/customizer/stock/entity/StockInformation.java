package com.quostomize.quostomize_be.domain.customizer.stock.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "stock_informations")
public class StockInformation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_information_id")
    private Long stockInformationId;

    @Column(name = "stock_code", nullable = false)
    private Integer stockCode;

    @Column(name = "stock_name", length = 30, nullable = false)
    private String stockName;

    @Column(name = "stock_present_price", nullable = false)
    private Integer stockPresentPrice;

    @Column(name = "stock_image", nullable = false)
    private String stockImage;
}
