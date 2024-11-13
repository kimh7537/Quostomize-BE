package com.quostomize.quostomize_be.api.hello.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.api.stock.dto.StockInformationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockBalanceService {


    @Value("${appkey}")
    private String appkey;

    @Value("${appsecret}")
    private String appSecret;

    @Value("${access_token}")
    private String accessToken;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public StockBalanceService(ObjectMapper objectMapper) {
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
        headers.set("tr_id", "TTTC8434R");
        headers.set("custtype", "P");
        return headers;
    }

    public Response getVolumeRank() {
        try {
            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/trading/inquire-balance")
                            .queryParam("CANO", "73935690")
                            .queryParam("ACNT_PRDT_CD", "01")
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
                    .headers(httpHeaders -> httpHeaders.addAll(createVolumeRankHttpHeaders()))
                    .retrieve()
                    .body(String.class);

            return parseFVolumeRank(response);
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            return null;  // 오류 시 빈 리스트 반환
        }
    }

    private Response parseFVolumeRank(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            Response stockInformationResponse = new Response();

            stockInformationResponse.setCtxAreaFk100(rootNode.path("ctx_area_fk100").asText());
            stockInformationResponse.setCtxAreaNk100(rootNode.path("ctx_area_nk100").asText());
            stockInformationResponse.setRtCd(rootNode.path("rt_cd").asText());
            stockInformationResponse.setMsgCd(rootNode.path("msg_cd").asText());
            stockInformationResponse.setMsg1(rootNode.path("msg1").asText());

            // Parsing output1
            List<Output1DTO> output1List = new ArrayList<>();
            JsonNode output1Node = rootNode.path("output1");
            if (output1Node.isArray()) {
                for (JsonNode node : output1Node) {
                    Output1DTO output1 = new Output1DTO();
                    output1.setPdno(node.path("pdno").asText());
                    output1.setPrdtName(node.path("prdt_name").asText());
                    output1.setTradDvsnName(node.path("trad_dvsn_name").asText());
                    output1.setBfdyBuyQty(node.path("bfdy_buy_qty").asText());
                    output1.setBfdySllQty(node.path("bfdy_sll_qty").asText());
                    output1.setThdtBuyqty(node.path("thdt_buyqty").asText());
                    output1.setThdtSllQty(node.path("thdt_sll_qty").asText());
                    output1.setHldgQty(node.path("hldg_qty").asText());
                    output1.setOrdPsblQty(node.path("ord_psbl_qty").asText());
                    output1.setPchsAvgPric(node.path("pchs_avg_pric").asText());
                    output1.setPchsAmt(node.path("pchs_amt").asText());
                    output1.setPrpr(node.path("prpr").asText());
                    output1.setEvluAmt(node.path("evlu_amt").asText());
                    output1.setEvluPflsAmt(node.path("evlu_pfls_amt").asText());
                    output1.setEvluPflsRt(node.path("evlu_pfls_rt").asText());
                    output1.setEvluErngRt(node.path("evlu_erng_rt").asText());
                    output1.setLoanDt(node.path("loan_dt").asText());
                    output1.setLoanAmt(node.path("loan_amt").asText());
                    output1.setStlnSlngChgs(node.path("stln_slng_chgs").asText());
                    output1.setExpdDt(node.path("expd_dt").asText());
                    output1.setFlttRt(node.path("fltt_rt").asText());
                    output1.setBfdyCprsIcdc(node.path("bfdy_cprs_icdc").asText());
                    output1.setItemMgnaRtName(node.path("item_mgna_rt_name").asText());
                    output1.setGrtaRtName(node.path("grta_rt_name").asText());
                    output1.setSbstPric(node.path("sbst_pric").asText());
                    output1.setStckLoanUnpr(node.path("stck_loan_unpr").asText());
                    output1List.add(output1);
                }
            }
            stockInformationResponse.setOutput1(output1List);

            // Parsing output2
            List<Output2DTO> output2List = new ArrayList<>();
            JsonNode output2Node = rootNode.path("output2");
            if (output2Node.isArray()) {
                for (JsonNode node : output2Node) {
                    Output2DTO output2 = new Output2DTO();
                    output2.setDncaTotAmt(node.path("dnca_tot_amt").asText());
                    output2.setNxdyExccAmt(node.path("nxdy_excc_amt").asText());
                    output2.setPrvsRcdlExccAmt(node.path("prvs_rcdl_excc_amt").asText());
                    output2.setCmaEvluAmt(node.path("cma_evlu_amt").asText());
                    output2.setBfdyBuyAmt(node.path("bfdy_buy_amt").asText());
                    output2.setThdtBuyAmt(node.path("thdt_buy_amt").asText());
                    output2.setNxdyAutoRdptAmt(node.path("nxdy_auto_rdpt_amt").asText());
                    output2.setBfdySllAmt(node.path("bfdy_sll_amt").asText());
                    output2.setThdtSllAmt(node.path("thdt_sll_amt").asText());
                    output2.setD2AutoRdptAmt(node.path("d2_auto_rdpt_amt").asText());
                    output2.setBfdyTlexAmt(node.path("bfdy_tlex_amt").asText());
                    output2.setThdtTlexAmt(node.path("thdt_tlex_amt").asText());
                    output2.setTotLoanAmt(node.path("tot_loan_amt").asText());
                    output2.setSctsEvluAmt(node.path("scts_evlu_amt").asText());
                    output2.setTotEvluAmt(node.path("tot_evlu_amt").asText());
                    output2.setNassAmt(node.path("nass_amt").asText());
                    output2.setFncgGldAutoRdptYn(node.path("fncg_gld_auto_rdpt_yn").asText());
                    output2.setPchsAmtSmtlAmt(node.path("pchs_amt_smtl_amt").asText());
                    output2.setEvluAmtSmtlAmt(node.path("evlu_amt_smtl_amt").asText());
                    output2.setEvluPflsSmtlAmt(node.path("evlu_pfls_smtl_amt").asText());
                    output2.setTotStlnSlngChgs(node.path("tot_stln_slng_chgs").asText());
                    output2.setBfdyTotAsstEvluAmt(node.path("bfdy_tot_asst_evlu_amt").asText());
                    output2.setAsstIcdcAmt(node.path("asst_icdc_amt").asText());
                    output2.setAsstIcdcErngRt(node.path("asst_icdc_erng_rt").asText());
                    output2List.add(output2);
                }
            }
            stockInformationResponse.setOutput2(output2List);

            return stockInformationResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
