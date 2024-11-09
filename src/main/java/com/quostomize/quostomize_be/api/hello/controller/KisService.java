package com.quostomize.quostomize_be.api.hello.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class KisService {

    @Value("${appkey}")
    private String appkey;

    @Value("${appsecret}")
    private String appSecret;

    @Value("${access_token}")
    private String accessToken;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KisService(ObjectMapper objectMapper) {
        this.restClient = RestClient.builder()
                .baseUrl("https://openapi.koreainvestment.com:9443")
                .build();
        this.objectMapper = objectMapper;
    }

    private HttpHeaders createVolumeRankHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("appkey", appkey);
        headers.set("appSecret", appSecret);
        headers.set("tr_id", "FHPST01710000");
        headers.set("custtype", "P");
        return headers;
    }

    public List<ResponseOutputDTO> getVolumeRank() {
        try {
            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/volume-rank")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_COND_SCR_DIV_CODE", "20171")
                            .queryParam("FID_INPUT_ISCD", "0002")
                            .queryParam("FID_DIV_CLS_CODE", "0")
                            .queryParam("FID_BLNG_CLS_CODE", "0")
                            .queryParam("FID_TRGT_CLS_CODE", "111111111")
                            .queryParam("FID_TRGT_EXLS_CLS_CODE", "000000")
                            .queryParam("FID_INPUT_PRICE_1", "0")
                            .queryParam("FID_INPUT_PRICE_2", "0")
                            .queryParam("FID_VOL_CNT", "0")
                            .queryParam("FID_INPUT_DATE_1", "0")
                            .build())
                    .headers(httpHeaders -> httpHeaders.addAll(createVolumeRankHttpHeaders()))
                    .retrieve()
                    .body(String.class);

            return parseFVolumeRank(response);
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            return new ArrayList<>();  // 오류 시 빈 리스트 반환
        }
    }

    private List<ResponseOutputDTO> parseFVolumeRank(String response) {
        try {
            List<ResponseOutputDTO> responseDataList = new ArrayList<>();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode outputNode = rootNode.get("output");
            if (outputNode != null) {
                for (JsonNode node : outputNode) {
                    ResponseOutputDTO responseData = new ResponseOutputDTO();
                    responseData.setHtsKorIsnm(node.get("hts_kor_isnm").asText());
                    responseData.setMkscShrnIscd(node.get("mksc_shrn_iscd").asText());
                    responseData.setDataRank(node.get("data_rank").asText());
                    responseData.setStckPrpr(node.get("stck_prpr").asText());
                    responseData.setPrdyVrssSign(node.get("prdy_vrss_sign").asText());
                    responseData.setPrdyVrss(node.get("prdy_vrss").asText());
                    responseData.setPrdyCtrt(node.get("prdy_ctrt").asText());
                    responseData.setAcmlVol(node.get("acml_vol").asText());
                    responseData.setPrdyVol(node.get("prdy_vol").asText());
                    responseData.setLstnStcn(node.get("lstn_stcn").asText());
                    responseData.setAvrgVol(node.get("avrg_vol").asText());
                    responseData.setNBefrClprVrssPrprRate(node.get("n_befr_clpr_vrss_prpr_rate").asText());
                    responseData.setVolInrt(node.get("vol_inrt").asText());
                    responseData.setVolTnrt(node.get("vol_tnrt").asText());
                    responseData.setNdayVolTnrt(node.get("nday_vol_tnrt").asText());
                    responseData.setAvrgTrPbmn(node.get("avrg_tr_pbmn").asText());
                    responseData.setTrPbmnTnrt(node.get("tr_pbmn_tnrt").asText());
                    responseData.setNdayTrPbmnTnrt(node.get("nday_tr_pbmn_tnrt").asText());
                    responseData.setAcmlTrPbmn(node.get("acml_tr_pbmn").asText());
                    responseDataList.add(responseData);
                }
            }
            return responseDataList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();  // 오류 시 빈 리스트 반환
        }
    }
}
