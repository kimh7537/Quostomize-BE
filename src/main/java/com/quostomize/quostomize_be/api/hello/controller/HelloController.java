package com.quostomize.quostomize_be.api.hello.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final TestService redisTestService;

    @GetMapping("/hello")
    @Operation(summary = "유저 프로필 이미지 삭제", description = "유저의 프로필 이미지를 삭제하고 기본 이미지로 설정합니다.")
    public String hello() {

//        throw new EntityNotFoundException();

        return "hello";
    }


    // 데이터 저장 엔드포인트
    @PostMapping("/save")
    public String saveData(@RequestParam String key, @RequestParam String value) {
        redisTestService.saveData(key, value);
        return "Data saved to Redis successfully!";
    }

    // 데이터 조회 엔드포인트
    @GetMapping("/get")
    public String getData(@RequestParam String key) {
        String value = redisTestService.getData(key);
        return value != null ? value : "No data found for key: " + key;
    }
}
