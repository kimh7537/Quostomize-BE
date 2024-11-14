package com.quostomize.quostomize_be.api.controller;

import com.quostomize.quostomize_be.domain.customizer.point.entity.CardPoint;
import com.quostomize.quostomize_be.domain.customizer.point.service.CardPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CardPointController {

    private final CardPointService cardPointService;

    @PostMapping("/api/point")
    public ResponseEntity<CardPoint> createCardPoint(@RequestParam Long cardId){
        CardPoint cardPoint = cardPointService.createCardPoint(cardId);
        return ResponseEntity.ok(cardPoint);
    }

}
