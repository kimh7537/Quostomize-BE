package com.quostomize.quostomize_be.api.member.controller;

import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @RequestMapping("/{memberId}")
    @Operation(summary = "단일 회원 정보 조회", description = "유저의 memberId를 pathVariable로 해서 회원 정보를 조회합니다.")
    public ResponseEntity<ResponseDTO<MemberResponseDTO>> findByMemberId(
            @Parameter(description = "정보를 조회하고 싶은 회원의 memberId")
            @PathVariable("memberId")
            String memberId
    ) {
        return ResponseEntity.ok(memberService.findByMemberId(memberId));
    }

    @Operation(summary = "전체 회원 정보 조회", description = "전체 회원 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseDTO<List<MemberResponseDTO>>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

}
