package com.quostomize.quostomize_be.api.controller;

import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import com.quostomize.quostomize_be.domain.customizer.point.service.CardPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CardPointController {

    private final CardPointService cardPointService;

    @Operation(summary = "카드 포인트 생성하기", description = "연결된 카드에 카드 포인트를 생성합니다.")
    @PostMapping("/api/point")
    public ResponseEntity<CardPoint> createCardPoint(@RequestParam Long cardId){
        CardPoint cardPoint = cardPointService.createCardPoint(cardId);
        return ResponseEntity.ok(cardPoint);
    }

}
