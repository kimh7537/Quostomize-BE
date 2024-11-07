package com.quostomize.quostomize_be.domain.customizer.stock.repository;

import com.quostomize.quostomize_be.api.hello.dto.StockInterestDto;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StockInterestRepository extends JpaRepository <StockInterest,Long> {

    // 모든 wishlist를 DTO로 조회합니다.
    @Query("Select NEW com.quostomize.quostomize_be.api.hello.dto.StockInterestDto(si.priority, sif.stockName, sif.stockPresentPrice) FROM StockInterest si JOIN StockInformation sif ON si.stockInformation = sif ORDER BY priority asc")
    List<StockInterestDto> findAllStockInterestDto();

    // 해당 id 에 맞는 wish항목을 삭제합니다.
    @Modifying
    @Transactional
    @Query("DELETE FROM StockInterest WHERE stockInterestId = :id")
    void DeleteStockById(@Param("id")Long id);

    //  3순위 에 위시리스트를 1순위로 옮기는 경우, 1순위 ->3순위 2순위 ->2순위  3순위 -> 1순위로 변경합니다.
    @Modifying
    @Transactional
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 1 THEN :newPriorityFor1 WHEN si.priority = :currentOrder THEN 1 ELSE si.priority END")
    void switchStock1(@Param("currentOrder") int currentOrder, @Param("newPriorityFor1") int newPriorityFor1);

    //  1. 2순위 에 위시리스트를 1순위로 옮기는 경우, 1순위를 ->4순위 2순위 -> 1순위 3순위 -> 2순위 로 변경합니다.
    @Modifying
    @Transactional
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 1 THEN 4 WHEN si.priority = :currentOrder THEN 1 WHEN si.priority = 3 THEN 2 ELSE si.priority END")
    void switchStock2(@Param("currentOrder") int currentOrder);

   //  2. 이후, 4순위 -> 3순위로 변경합니다.
    @Modifying
    @Transactional
    @Query("UPDATE StockInterest si SET si.priority = 3 WHERE si.priority = :newPriorityFor1")
    void switchStock3( @Param("newPriorityFor1") int newPriorityFor1);

}
