package com.quostomize.quostomize_be.domain.customizer.stock.repository;

import com.quostomize.quostomize_be.api.stock.dto.StockAddInterest;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockInterestRepository extends JpaRepository <StockInterest,Long> {

    // 모든 wishlist를 DTO로 조회합니다.
    @Query("Select si.priority, sif.stockName, sif.stockPresentPrice, sif.stockImage FROM StockInterest si JOIN FETCH StockInformation sif ON si.stockInformation = sif WHERE si.customer.cardDetail.cardSequenceId = :cardId ORDER BY priority asc")
    List<Object[]> findAllStockInterestDto(@Param("cardId")Long cardId);

    // 해당 order 에 맞는 wish항목을 삭제합니다.
    @Modifying
    @Query("DELETE FROM StockInterest si WHERE si.priority = :od AND si.customer.cardDetail.cardSequenceId = :cardId")
    void DeleteStockById(@Param("od")int order ,@Param("cardId")Long cardId);

    // 위시리스트 순위 1위를 삭제한 경우, 2순위를 1순위로 , 3순위를 2순위로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 2 THEN 1 WHEN si.priority = 3 THEN 2 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStockDeleteOrder1(@Param("cardId")Long cardId);

    // 위시리스트 순위 2위를 삭제한 경우, 3순위를 2순위로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 3 THEN 2 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStockDeleteOrder2(@Param("cardId")Long cardId);

    //  3순위 에 위시리스트를 1순위로 옮기는 경우, 1순위 ->2순위 2순위 ->3순위  3순위 -> 1순위로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 1 THEN 2 WHEN si.priority = :currentOrder THEN 1 WHEN si.priority = 2 THEN 3 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStock1(@Param("currentOrder") int currentOrder ,@Param("cardId")Long cardId);

    //  3순위에서 2순위로 옮기는 경우, 2순위는 3순위로 현재순위(3순위)는 2순위로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 2 THEN 3 WHEN si.priority = :currentOrder THEN 2 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStock3(@Param("currentOrder") int currentOrder ,@Param("cardId")Long cardId);

    //  2순위 에 위시리스트를 1순위로 옮기는 경우, 1순위를 ->2순위 2순위 -> 1순위 3순위 -> 3순위 로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 1 THEN 2 WHEN si.priority = :currentOrder THEN 1 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStock2(@Param("currentOrder") int currentOrder ,@Param("cardId")Long cardId);

    // 2순위에 위시리스트를 3순위로 옮기는 경우,  3순위를 2순위로, 현재순위(2순위)를 3순위로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 3 THEN 2 WHEN si.priority = :currentOrder THEN 3 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStock4(@Param("currentOrder") int currentOrder ,@Param("cardId")Long cardId);

    // 1순위를 2순위로 옮기는 경우, 1순위는 2순위로, 2순위는 1순위로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 2 THEN 1 WHEN si.priority = :currentOrder THEN 2 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStock5(@Param("currentOrder") int currentOrder ,@Param("cardId")Long cardId);

    // 1순위를 3순위로 옮기는 경우, 2순위는 1순위로, 3순위는 2순위로 , 1순위는 3순위로 변경합니다.
    @Modifying
    @Query("UPDATE StockInterest si SET si.priority = CASE WHEN si.priority = 2 THEN 1  WHEN si.priority = 3 THEN 2 WHEN si.priority = :currentOrder THEN 3 ELSE si.priority END WHERE si.customer.cardDetail.cardSequenceId = :cardId")
    void switchStock6(@Param("currentOrder") int currentOrder ,@Param("cardId")Long cardId);


    @Query("SELECT new com.quostomize.quostomize_be.api.stock.dto.StockAddInterest(c, COUNT(si)) " +
            "FROM Customer c " +
            "LEFT JOIN c.stockInterests si " +
            "JOIN c.member m " +
            "WHERE m.memberId = :memberId " +
            "GROUP BY c")
    Optional<StockAddInterest> findCustomerWithStockInterest(@Param("memberId") Long memberId);



}
