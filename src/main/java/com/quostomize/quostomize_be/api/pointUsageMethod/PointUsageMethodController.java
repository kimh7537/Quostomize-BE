package com.quostomize.quostomize_be.api.pointUsageMethod;

import com.quostomize.quostomize_be.domain.customizer.point.entity.PointUsageMethod;
import com.quostomize.quostomize_be.domain.customizer.point.service.PointUsageMethodService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PointUsageMethodController {

    @Autowired
    private PointUsageMethodService pointUsageMethodService;

    @PostMapping("/api/point-usage-method")
    @Operation(summary = "포인트 사용 방법 생성하기", description = "연결된 카드에 포인트 사용 방법을 생성합니다.")
    public ResponseEntity<PointUsageMethod> createPointUsageMethod(@RequestParam Long cardId, @RequestParam Boolean stock, @RequestParam Boolean lotto, @RequestParam Boolean payback){
        PointUsageMethod pointUsageMethod = pointUsageMethodService.CreatePointUsageMethod(cardId,stock,lotto,payback);
        return ResponseEntity.ok(pointUsageMethod);
    }

}
