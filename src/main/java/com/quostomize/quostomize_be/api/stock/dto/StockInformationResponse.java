package com.quostomize.quostomize_be.api.stock.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record StockInformationResponse(
        List<StockOneResponse> output1,
        List<StockAllResponse> output2,
        String rtCd,
        String msgCd,
        String msg1
) {

    @Getter
    @Setter
    public static class StockOneResponse {
        private String pdno; // 종목코드
        private String prdtName; // 종목이름
        private String pchsAmt; // 매입금액
        private String prpr; // 현재가
        private String evluAmt; // 현재 모든 합계 가격
        private String flttRt; // 등락율
        private String hldgQty; // 보유수량
        private String evluPflsRt; // 평가 손익율
    }

    @Getter
    @Setter
    public static class StockAllResponse {
        private String evluAmtSmtlAmt; // 평가금액합계금액
        private String pchsAmtSmtlAmt; // 매입금액합계금액
    }
}