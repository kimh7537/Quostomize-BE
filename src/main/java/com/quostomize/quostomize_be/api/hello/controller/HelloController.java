package com.quostomize.quostomize_be.api.hello.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping("/hello")
    @Operation(summary = "유저 프로필 이미지 삭제", description = "유저의 프로필 이미지를 삭제하고 기본 이미지로 설정합니다.")
    public String hello() {

        throw new EntityNotFoundException();

//        return "hello";
    }
}
