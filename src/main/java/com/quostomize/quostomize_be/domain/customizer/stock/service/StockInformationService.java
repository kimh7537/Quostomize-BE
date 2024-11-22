package com.quostomize.quostomize_be.domain.customizer.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.stock.dto.StockInformationResponse;
import com.quostomize.quostomize_be.api.stock.dto.StockSearchResponse;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.common.error.exception.JsonProcessingAppException;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockAccount;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockHolding;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockInformation;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockAccountRepository;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockHoldingRepository;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInformationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@Transactional(readOnly = true)
public class StockInformationService {

    @Value("${appkey}")
    private String appkey;

    @Value("${appsecret}")
    private String appSecret;

    private final StockInformationRepository stockInformationRepository;
    private final StockHoldingRepository stockHoldingRepository;
    private final StockAccountRepository stockAccountRepository;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public StockInformationService(StockInformationRepository stockInformationRepository, StockHoldingRepository stockHoldingRepository, StockAccountRepository stockAccountRepository, ObjectMapper objectMapper) {
        this.stockInformationRepository = stockInformationRepository;
        this.stockHoldingRepository = stockHoldingRepository;
        this.stockAccountRepository = stockAccountRepository;
        this.restClient = RestClient.builder()
                .baseUrl("https://openapi.koreainvestment.com:9443")
                .build();
        this.objectMapper = objectMapper;
    }
    

    @Transactional
    public StockInformationResponse showStockInformation(Long stockAccountId) {
        StockAccount stockAccount = stockAccountRepository.findById(stockAccountId)
                .orElseThrow(() -> new EntityNotFoundException("주식 계좌 정보를 찾을 수 없음"));
        String openAPIAccessToken = Optional.ofNullable(stockAccount.getOpenAPIToken())
                .filter(token -> stockAccount.getExpiryDate() != null && LocalDateTime.now().isBefore(stockAccount.getExpiryDate()))
                .orElseGet(() -> getOpenAPIAccessToken(stockAccountId));
        String response = retrieveStockInformationFromAPI(openAPIAccessToken, stockAccount);

        try {
            saveStockInformationAndHolding(response, stockAccount);
            return parseForStockInformation(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류 발생", e);
        }
    }

    //OpenAPI로 access token값을 받기 위한 메서드
    @Transactional
    public String getOpenAPIAccessToken(Long stockAccountId) {
        Map<String, String> requestBody = Map.of(
                "grant_type", "client_credentials",
                "appkey", appkey,
                "appsecret", appSecret
        );

        String response = restClient.post()
                .uri("/oauth2/tokenP")
                .header("Content-Type", "application/json")  // JSON 형식으로 전송
                .body(requestBody) // JSON 요청 바디 설정
                .retrieve()
                .body(String.class); // RestClientException 발생 시 GlobalExceptionHandler에서 처리

        return parseAndSaveAccessToken(stockAccountId, response);
    }

    private String parseAndSaveAccessToken(Long stockAccountId, String response){
        try{
            JsonNode rootNode = objectMapper.readTree(response);
            String openAPIToken = parseText(rootNode, "access_token");
            LocalDateTime expiryDate = LocalDateTime.parse(
                    parseText(rootNode, "access_token_token_expired"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            updateAccessToken(stockAccountId, openAPIToken, expiryDate);
            return openAPIToken;
        }catch (JsonProcessingException e){
            throw new JsonProcessingAppException(e);
        }
    }

    @Transactional
    protected void updateAccessToken(Long stockAccountId, String openAPIToken, LocalDateTime expiryDate) {
        // 특정 stockAccountId로 StockAccount 조회
        StockAccount stockAccount = stockAccountRepository.findById(stockAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Stock account not found"));
        // 토큰과 만료 날짜 업데이트
        stockAccount.updateAccessTokenInfo(openAPIToken, expiryDate);
        // 업데이트된 정보 저장
        stockAccountRepository.save(stockAccount);
    }


    private String retrieveStockInformationFromAPI(String openAPIAccessToken, StockAccount stockAccount) {
        String accountNumber = stockAccount.getStockAccountNumber().toString();
        String cano = accountNumber.substring(0, 8);
        String acntPrdtCd = accountNumber.substring(8, 10);
        return retrieveStockInformation(openAPIAccessToken, cano, acntPrdtCd);
    }


    private String retrieveStockInformation(String openAPIAccessToken, String cano, String acntPrdtCd) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/trading/inquire-balance")
                        .queryParam("CANO", cano)
                        .queryParam("ACNT_PRDT_CD", acntPrdtCd)
                        .queryParam("AFHR_FLPR_YN", "N")
                        .queryParam("OFL_YN", "")
                        .queryParam("INQR_DVSN", "02")
                        .queryParam("UNPR_DVSN", "01")
                        .queryParam("FUND_STTL_ICLD_YN", "N")
                        .queryParam("FNCG_AMT_AUTO_RDPT_YN", "N")
                        .queryParam("PRCS_DVSN", "00")
                        .queryParam("CTX_AREA_FK100", "")
                        .queryParam("CTX_AREA_NK100", "")
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(showStockInformationHttpHeaders(openAPIAccessToken)))
                .retrieve()
                .body(String.class);
    }

    private HttpHeaders showStockInformationHttpHeaders(String openAPIAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAPIAccessToken);
        headers.set("appkey", appkey);
        headers.set("appSecret", appSecret);
        headers.set("tr_id", "TTTC8434R");
        headers.set("custtype", "P");
        return headers;
    }

    private StockInformationResponse parseForStockInformation(String response) throws JsonProcessingException{
            JsonNode rootNode = objectMapper.readTree(response);
            // 상위 정보 파싱
            String rtCd = parseText(rootNode, "rt_cd");
            String msg1 = parseText(rootNode, "msg1");
            // output 리스트 파싱
            List<StockInformationResponse.StockOneResponse> stockOneResponses = parseStockOneResponses(rootNode.path("output1"));
            List<StockInformationResponse.StockAllResponse> stockAllResponses = parseStockAllResponses(rootNode.path("output2"));
            // StockInformationResponse 객체를 생성하여 반환
            return new StockInformationResponse(stockOneResponses, stockAllResponses, rtCd, msg1);
    }


    // Helper method to parse StockOneResponse list
    private List<StockInformationResponse.StockOneResponse> parseStockOneResponses(JsonNode output1Node) {
        List<StockInformationResponse.StockOneResponse> stockOneResponses = new ArrayList<>();
        if (output1Node.isArray()) {
            for (JsonNode node : output1Node) {
                StockInformationResponse.StockOneResponse stockOne = new StockInformationResponse.StockOneResponse();
//                stockOne.setPdno(parseText(node, "pdno")); //종목코드
                stockOne.setPrdtName(parseText(node, "prdt_name")); //종목이름
//                stockOne.setPchsAmt(parseText(node, "pchs_amt")); //매수가
                stockOne.setPrpr(parseText(node, "prpr")); //현재가 1개
//                stockOne.setEvluAmt(parseText(node, "evlu_amt")); //현재가 여러개
//                stockOne.setFlttRt(parseText(node, "fltt_rt")); //등략율
                stockOne.setHldgQty(parseText(node, "hldg_qty")); //보유 주식 수
                stockOne.setEvluPflsRt(parseText(node, "evlu_pfls_rt")); //매수가와 현재가 등략율
                stockOne.setStockImage(getStockInformationURL(parseText(node, "pdno")));
                stockOneResponses.add(stockOne);
            }
        }
        return stockOneResponses;
    }

    // StockInformation에서 최신화된 이미지 URL을 가져옴
    private String getStockInformationURL(String stockCode) {
        return stockInformationRepository.findByStockCode(Integer.valueOf(stockCode))
                .map(StockInformation::getStockImage)
                .orElseThrow(() -> new EntityNotFoundException("엔티티를 찾을 수 없음"));
    }


    // Helper method to parse StockAllResponse list
    private List<StockInformationResponse.StockAllResponse> parseStockAllResponses(JsonNode output2Node) {
        List<StockInformationResponse.StockAllResponse> stockAllResponses = new ArrayList<>();
        if (output2Node.isArray()) {
            for(JsonNode node : output2Node){
                StockInformationResponse.StockAllResponse stockAllResponse = new StockInformationResponse.StockAllResponse();
                int evluAmtSmtlAmt = Integer.parseInt(parseText(node, "evlu_amt_smtl_amt"));
                int pchsAmtSmtlAmt = Integer.parseInt(parseText(node, "pchs_amt_smtl_amt"));
                // 계산 후 소수점 두 자리로 반올림
                double percentageChange = ((double) (evluAmtSmtlAmt - pchsAmtSmtlAmt) / pchsAmtSmtlAmt) * 100;
                BigDecimal roundedPercentage = BigDecimal.valueOf(percentageChange).setScale(2, RoundingMode.HALF_UP);
                stockAllResponse.setEvluAmtSmtlAmt(evluAmtSmtlAmt);
                stockAllResponse.setResultRate(roundedPercentage.doubleValue());
                stockAllResponses.add(stockAllResponse);
            }
        }
        return stockAllResponses;
    }

    private void saveStockInformationAndHolding(String response, StockAccount stockAccount)
            throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode output1 = rootNode.get("output1");
        for (JsonNode stock : output1) {
            StockInformation stockInfo = processStockInformation(stock);
            processStockHolding(stock, stockAccount, stockInfo);
        }
    }

    private StockInformation processStockInformation(JsonNode stock) {
        StockInformationData data = extractStockInformationData(stock);
        return updateOrCreateStockInformation(data);
    }

    private StockInformationData extractStockInformationData(JsonNode stock) {
        return StockInformationData.builder()
                .stockCode(Integer.parseInt(parseText(stock, "pdno")))
                .stockName(parseText(stock, "prdt_name"))
                .presentPrice(Integer.parseInt(parseText(stock, "prpr")))
                .build();
    }


    //주식 정보 데이터가 이미 존재하면 업데이트, 없으면 새로 만들어서 저장함
    //현재가 정보를 여러 명의 사용자가 동시에 접근해서 업데이트하면 오류가 발생할 수도 있음 -> 동시성 synchronized
    private synchronized StockInformation updateOrCreateStockInformation(StockInformationData data) {
        return stockInformationRepository.findByStockCode(data.getStockCode())
                .map(existing -> {
                    existing.updatePresentPrice(data.getPresentPrice());
                    return stockInformationRepository.save(existing); // 명시적 save 호출
                })
                .orElseGet(() -> createNewStockInformation(data));
    }

    //새로 주식 정보를 만들어서 저장함
    private StockInformation createNewStockInformation(StockInformationData data) {
        StockInformation newStock = StockInformation.builder()
                .stockCode(data.getStockCode())
                .stockName(data.getStockName())
                .stockPresentPrice(data.getPresentPrice())
                .stockImage("image/quokka.png")
                .build();
        return stockInformationRepository.save(newStock);
    }

    private void processStockHolding(JsonNode stock, StockAccount stockAccount, StockInformation stockInfo) {
        int holdingQuantity = Integer.parseInt(parseText(stock, "hldg_qty"));

        if (holdingQuantity > 0) {
            updateOrCreateStockHolding(stock, stockAccount, stockInfo);
        } else {
            deleteStockHolding(stockAccount, stockInfo);
        }
    }

    private void updateOrCreateStockHolding(JsonNode stock, StockAccount stockAccount, StockInformation stockInfo) {
        long purchaseAmount = Long.parseLong(stock.get("pchs_amt").asText());
        StockHolding stockHolding = findOrCreateStockHolding(stockAccount, stockInfo);
        stockHolding.updateStockTotalMoney(purchaseAmount);
        stockHoldingRepository.save(stockHolding);
    }

    private StockHolding findOrCreateStockHolding(StockAccount stockAccount, StockInformation stockInfo) {
        return stockHoldingRepository
                .findByStockAccountAndStockInformation(stockAccount, stockInfo)
                .orElseGet(() -> StockHolding.builder()
                        .stockAccount(stockAccount)
                        .stockInformation(stockInfo)
                        .build());
    }

    private void deleteStockHolding(StockAccount stockAccount, StockInformation stockInfo) {
        stockHoldingRepository
                .findByStockAccountAndStockInformation(stockAccount, stockInfo)
                .ifPresent(stockHoldingRepository::delete);
    }

    @Getter
    @Builder
    private static class StockInformationData {
        private final Integer stockCode;
        private final String stockName;
        private final Integer presentPrice;
    }

    private String parseText(JsonNode node, String fieldName) {
        return node.path(fieldName).asText();
    }



    //JPA로 주식 검색 함수 구현
    public List<StockSearchResponse> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        return stockInformationRepository.findByKeyword(keyword).stream()
                .map(StockSearchResponse::from)
                .collect(Collectors.toList());
    }


}
