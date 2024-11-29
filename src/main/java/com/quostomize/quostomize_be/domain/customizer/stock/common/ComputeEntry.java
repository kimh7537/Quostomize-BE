package com.quostomize.quostomize_be.domain.customizer.stock.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComputeEntry {


    public List<Map.Entry<String, HashMap<String, Integer>>> getListEntry(HashMap<String, HashMap<String, Integer>> setName){
        List<Map.Entry<String, HashMap<String, Integer>>> entryListSet = setName.entrySet().stream() .sorted((entry1, entry2) -> {
                    // 내부 HashMap에서 가장 큰 값을 찾고 내림차순으로 비교
                    int max1 = Collections.max(entry1.getValue().values());
                    int max2 = Collections.max(entry2.getValue().values());
                    if (max1 == max2) {
                        // 값이 동일할 경우, 랜덤으로 순서를 변경하거나 안 변경하도록 설정
                        return Math.random() < 0.5 ? -1 : 1; // 50% 확률로 순서 변경
                    }
                    return Integer.compare(max2, max1);
                })
                .collect(Collectors.toList());
        return entryListSet;
    }
}
