package com.quostomize.quostomize_be.api.registration.controller;

import com.quostomize.quostomize_be.api.registration.dto.MemberFormDto;
import com.quostomize.quostomize_be.domain.customizer.registration.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/register")
    public ResponseEntity<MemberFormDto> memberForm() {
        MemberFormDto memberFormDto = new MemberFormDto();
        return ResponseEntity.ok(memberFormDto);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerMember(@Valid @RequestBody MemberFormDto memberFormDto) {
        // MemberService의 saveMember 메서드를 호출
        memberService.saveMember(memberFormDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}