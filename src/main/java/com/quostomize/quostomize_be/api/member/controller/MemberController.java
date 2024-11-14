package com.quostomize.quostomize_be.api.member.controller;

import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @RequestMapping("/{memberId}")
    public ResponseEntity<ResponseDTO<MemberResponseDTO>> findByMemberId(@PathVariable("memberId") String memberId) {
        return ResponseEntity.ok(memberService.findByMemberId(memberId));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MemberResponseDTO>>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

}
