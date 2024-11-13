package com.quostomize.quostomize_be.domain.customizer.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.stock.dto.StockInformationResponse;
import com.quostomize.quostomize_be.common.error.exception.JsonProcessingAppException;
import com.quostomize.quostomize_be.domain.customizer.stock.entity.StockAccount;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockAccountRepository;
import com.quostomize.quostomize_be.domain.customizer.stock.repository.StockInformationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockInformationService {

    @Value("${appkey}")
    private String appkey;

    @Value("${appsecret}")
    private String appSecret;

    @Value("${access_token}")
    private String accessToken;

    private final StockInformationRepository stockInformationRepository;
    private final StockAccountRepository stockAccountRepository;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public StockInformationService(StockInformationRepository stockInformationRepository, StockAccountRepository stockAccountRepository, ObjectMapper objectMapper) {
        this.stockInformationRepository = stockInformationRepository;
        this.stockAccountRepository = stockAccountRepository;
        this.restClient = RestClient.builder()
                .baseUrl("https://openapi.koreainvestment.com:9443")
                .build();
        this.objectMapper = objectMapper;
    }

    public String getOpenAPIAccessToken() {
        Map<String, String> requestBody = Map.of(
                "grant_type", "client_credentials",
                "appkey", appkey,
                "appsecret", appSecret
        );

        return restClient.post()
                .uri("/oauth2/tokenP")
                .header("Content-Type", "application/json")  // JSON 형식으로 전송
                .body(requestBody) // JSON 요청 바디 설정
                .retrieve()
                .body(String.class); // RestClientException 발생 시 GlobalExceptionHandler에서 처리
    }


    public StockInformationResponse showStockInformation(long stockAccountId){
        StockAccount stockAccount = stockAccountRepository.findById(stockAccountId)
                .orElseThrow(() -> new EntityNotFoundException("주식 계좌 정보를 찾을 수 없음"));

        String stockAccountNumberStr = stockAccount.getStockAccountNumber().toString();
        String cano = stockAccountNumberStr.substring(0, 8);
        String acntPrdtCd = stockAccountNumberStr.substring(8, 10);

        String response = retrieveStockInformation(cano, acntPrdtCd);
        return parseForStockInformation(response);
    }

    private HttpHeaders showStockInformationHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("appkey", appkey);
        headers.set("appSecret", appSecret);
        headers.set("tr_id", "TTTC8434R");
        headers.set("custtype", "P");
        return headers;
    }

    private String retrieveStockInformation(String cano, String acntPrdtCd) {
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
                .headers(httpHeaders -> httpHeaders.addAll(showStockInformationHttpHeaders()))
                .retrieve()
                .body(String.class);
    }


    private StockInformationResponse parseForStockInformation(String response){
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            // 상위 정보 파싱
            String rtCd = parseText(rootNode, "rt_cd");
            String msgCd = parseText(rootNode, "msg_cd");
            String msg1 = parseText(rootNode, "msg1");
            // output 리스트 파싱
            List<StockInformationResponse.StockOneResponse> stockOneResponses = parseStockOneResponses(rootNode.path("output1"));
            List<StockInformationResponse.StockAllResponse> stockAllResponses = parseStockAllResponses(rootNode.path("output2"));
            // StockInformationResponse 객체를 생성하여 반환
            return new StockInformationResponse(stockOneResponses, stockAllResponses, rtCd, msgCd, msg1);

        }catch (JsonProcessingException e){
            throw new JsonProcessingAppException(e);
        }
    }


    // Helper method to parse StockOneResponse list
    private List<StockInformationResponse.StockOneResponse> parseStockOneResponses(JsonNode output1Node) {
        List<StockInformationResponse.StockOneResponse> stockOneResponses = new ArrayList<>();
        if (output1Node.isArray()) {
            for (JsonNode node : output1Node) {
                StockInformationResponse.StockOneResponse stockOne = new StockInformationResponse.StockOneResponse();
                stockOne.setPdno(parseText(node, "pdno"));
                stockOne.setPrdtName(parseText(node, "prdt_name"));
                stockOne.setPchsAmt(parseText(node, "pchs_amt"));
                stockOne.setPrpr(parseText(node, "prpr"));
                stockOne.setEvluAmt(parseText(node, "evlu_amt"));
                stockOne.setFlttRt(parseText(node, "fltt_rt"));
                stockOne.setHldgQty(parseText(node, "hldg_qty"));
                stockOne.setEvluPflsRt(parseText(node, "evlu_pfls_rt"));
                stockOneResponses.add(stockOne);
            }
        }
        return stockOneResponses;
    }

    // Helper method to parse StockAllResponse list
    private List<StockInformationResponse.StockAllResponse> parseStockAllResponses(JsonNode output2Node) {
        List<StockInformationResponse.StockAllResponse> stockAllResponses = new ArrayList<>();
        if (output2Node.isArray()) {
            for (JsonNode node : output2Node) {
                StockInformationResponse.StockAllResponse stockAll = new StockInformationResponse.StockAllResponse();
                stockAll.setEvluAmtSmtlAmt(parseText(node, "evlu_amt_smtl_amt"));
                stockAll.setPchsAmtSmtlAmt(parseText(node, "pchs_amt_smtl_amt"));
                stockAllResponses.add(stockAll);
            }
        }
        return stockAllResponses;
    }

    private String parseText(JsonNode node, String fieldName) {
        return node.path(fieldName).asText();
    }


}
