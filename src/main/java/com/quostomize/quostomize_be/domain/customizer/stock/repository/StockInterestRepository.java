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

    // 해당 order 에 맞는 wish항목을 삭제합니다.
    @Modifying
    @Transactional
    @Query("DELETE FROM StockInterest si WHERE si.priority = :od")
    void DeleteStockById(@Param("od")int order);

    // 위시리스트 순위 1위를 삭제한 경우, 2순위를 1순위로 , 3순위를 2순위로 변경합니다.
    @Modifying
    @Transactional
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 2 THEN 1 WHEN si.priority = 3 THEN 2 ELSE si.priority END")
    void switchStockDeleteOrder1();

    // 위시리스트 순위 2위를 삭제한 경우, 3순위를 2순위로 변경합니다.
    @Modifying
    @Transactional
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 3 THEN 2 ELSE si.priority END")
    void switchStockDeleteOrder2();

    //  3순위 에 위시리스트를 1순위로 옮기는 경우, 1순위 ->2순위 2순위 ->3순위  3순위 -> 1순위로 변경합니다.
    @Modifying
    @Transactional
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 1 THEN 2 WHEN si.priority = :currentOrder THEN 1 WHEN si.priority = 2 THEN 3 ELSE si.priority END")
    void switchStock1(@Param("currentOrder") int currentOrder);

    //  2순위 에 위시리스트를 1순위로 옮기는 경우, 1순위를 ->2순위 2순위 -> 1순위 3순위 -> 3순위 로 변경합니다.
    @Modifying
    @Transactional
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 1 THEN 2 WHEN si.priority = :currentOrder THEN 1 ELSE si.priority END")
    void switchStock2(@Param("currentOrder") int currentOrder);

}
