package com.quostomize.quostomize_be.domain.customizer.stock.common;

import com.quostomize.quostomize_be.api.stock.dto.CardBenefitResponse;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class CategorizingBenefits {

    // 상위분류 개수
    private int upperNumber = 0;
    // 하위분류 개수
    private int lowerNumber = 0;

    // 하위분류 이름
    private HashMap<String, HashMap<String, Integer>> lowerName = new HashMap<String, HashMap<String, Integer>>();
    // 상위분류 이름
    private HashMap<String, HashMap<String, Integer>> upperName = new HashMap<String, HashMap<String, Integer>>();

    public void getListHashMap(List<CardBenefitResponse> benefits){

        for (CardBenefitResponse benefit : benefits) {

            if (benefit.isActive() == true) { // 혜택적립이 활성화 되어 있다면,
                // 상위 분류- 하위 분류 이름, 적립률
                HashMap<String, Integer> upper = new HashMap<String, Integer>();
                // 하위 분류 - 하위 분류 이름, 적립률
                HashMap<String, Integer> lower = new HashMap<String, Integer>();
                if (benefit.franchiseName() == null) { // 하위분류가 null 일 때, <상위분류만 체크돼있을 떄>
                    upperNumber += 1; // 상위분류 개수 추가
                    upper.put(benefit.franchiseName(), benefit.benefitRate());
                    upperName.put(benefit.benefitCategoryType(), upper); // 상위분류 이름 및 적립률 추가
                } else { // 하위분류가 체크되어있을때,
                    lowerNumber += 1; // 하위분류 개수 추가
                    lower.put(benefit.franchiseName(), benefit.benefitRate());
                    lowerName.put(benefit.benefitCategoryType(), lower); // 하위분류 이름 및 적립률 추가
                }
            }
        }
   }
}
