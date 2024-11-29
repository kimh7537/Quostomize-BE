package com.quostomize.quostomize_be.domain.customizer.stock.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ComputeWithLowerAndUpper {

    List<Map.Entry<String, HashMap<String, Integer>>> entryListLower = List.of();
    List<Map.Entry<String, HashMap<String, Integer>>> entryListUpper = List.of();

    public void computeLower(HashMap<String, HashMap<String, Integer>> setName,ComputeEntry computeEntry){
        if(setName.size() > 1){
            entryListLower = computeEntry.getListEntry(setName);
        } else {
            entryListLower = setName.entrySet().stream().toList();
        }
    }

    public void computeUpper(HashMap<String, HashMap<String, Integer>> setName,ComputeEntry computeEntry){
        if(setName.size() > 1){
            entryListUpper = computeEntry.getListEntry(setName);
        } else {
            entryListUpper = setName.entrySet().stream().toList();
        }
    }
}
