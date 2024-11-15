package com.quostomize.quostomize_be.api.pointUsageMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quostomize.quostomize_be.domain.customizer.card.entity.CardDetail;
import com.quostomize.quostomize_be.domain.customizer.card.repository.CardRepository;
import com.quostomize.quostomize_be.domain.customizer.point.entity.PointUsageMethod;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PointUsageMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardRepository cardRepository;

    @Test
    void createPointUsageMethod() throws Exception {
        //given
        Boolean payback = false;
        Boolean lotto = true;
        Boolean stock =true;
        PointUsageMethod pointUsageMethod;
        CardDetail cardDetail = cardRepository.findById(32L).orElseThrow(() -> new EntityNotFoundException("카드 아이디가 유효하지 않습니다."));
        if(payback == true && stock == true){ // 조각투자와 페이백이 동시에 진행 시 예외처리 진행
            throw new EntityNotFoundException("페이벡과 주식투자가 동시에 체크되어 있습니다.");
        } else{ // 예외처리 확인 후, 카드 정보를 참조하는 포인트 사용방법 객체 생성
            pointUsageMethod = new PointUsageMethod(lotto,payback,stock, cardDetail);
        }
        String json = objectMapper.writeValueAsString(pointUsageMethod);

        // MultiValueMap 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // key에 대한 여러 값 추가
        params.add("cardId","32");
        params.add("stock", "true");
        params.add("lotto", "true");
        params.add("payback", "false");

        //then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/point-usage-method").contentType(MediaType.APPLICATION_JSON).content(json).params(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}