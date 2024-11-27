package com.quostomize.quostomize_be.domain.customizer.stock.common;

import com.quostomize.quostomize_be.api.stock.dto.StockRecommendResponse;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInformationRepository;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class UpperAndLowerClassification {

    private int lowerIndex = 0; // 하위 인덱스 진행 사이클 수
    private int upperIndex = 0; // 상위 인덱스 진행 사이클 수

    public void computeByAll(StockInformationRepository stockInformationRepository, String preSignedUrl
            , List<StockRecommendResponse> recommendResponses
            , ComputeWithLowerAndUpper computeWithLowerAndUpper){

        UpperAndLowerClassificationByIndex upperAndLowerClassificationByIndex = new UpperAndLowerClassificationByIndex();

        for (int i = 0; i < 3; i++) {
            if (lowerIndex < computeWithLowerAndUpper.getEntryListLower().size() || upperIndex < computeWithLowerAndUpper.getEntryListUpper().size()) {
                // 로어 인덱스가 실제 사이즈랑 일치 시, -> 어퍼만 비교하면 되기에, 어퍼만 비교하는 로직을 추가한다.
                if (lowerIndex == computeWithLowerAndUpper.getEntryListLower().size()) {
                    Map.Entry<String, HashMap<String, Integer>> entry = computeWithLowerAndUpper.getEntryListUpper().get(i);
                    upperAndLowerClassificationByIndex.getUpperList(entry,stockInformationRepository,preSignedUrl,recommendResponses);
                    upperIndex += 1;

                } else if (upperIndex == computeWithLowerAndUpper.getEntryListUpper().size()) { //반대로 어퍼 인덱스가 실제 사이즈랑 일치 시, 로어 인덱스만 비교한다.
                    // 하위 인덱스를 진행시
                    Map.Entry<String, HashMap<String, Integer>> entry = computeWithLowerAndUpper.getEntryListLower().get(i);
                    upperAndLowerClassificationByIndex.getLowerList(entry,stockInformationRepository,preSignedUrl,recommendResponses);
                    lowerIndex += 1;

                } else if (computeWithLowerAndUpper.getEntryListLower().get(lowerIndex).getValue().entrySet().stream().findFirst().get().getValue() > computeWithLowerAndUpper.getEntryListUpper().get(upperIndex).getValue().entrySet().stream().findFirst().get().getValue()) { // 혜택률이 하위 분류가 더 큰 경우
                    // 하위 인덱스를 진행시
                    Map.Entry<String, HashMap<String, Integer>> entry = computeWithLowerAndUpper.getEntryListLower().get(i);
                    upperAndLowerClassificationByIndex.getLowerList(entry,stockInformationRepository,preSignedUrl,recommendResponses);
                    lowerIndex += 1;

                } else { // 상위 인덱스를 진행 시
                    Map.Entry<String, HashMap<String, Integer>> entry = computeWithLowerAndUpper.getEntryListUpper().get(i);
                    upperAndLowerClassificationByIndex.getUpperList(entry,stockInformationRepository,preSignedUrl,recommendResponses);
                    upperIndex += 1;
                }
            }
        }
    }
}
