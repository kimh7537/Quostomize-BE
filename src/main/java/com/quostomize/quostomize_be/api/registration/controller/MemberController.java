package com.quostomize.quostomize_be.api.registration.controller;

import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.registration.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

//    @GetMapping(value = "/register")
//    @Operation(summary = "회원 조회", description = "ID로 회원 정보를 조회합니다.")
//    public ResponseEntity<ResponseDTO> memberForm() {
//        MemberRequestDto memberRequestDto = new MemberRequestDto("","","","","","null","","","","","","","null");
//        ResponseDTO responseDTO = new ResponseDTO(memberRequestDto);
//        return ResponseEntity.ok(responseDTO);
//    }

    @PostMapping(value = "/register")
    @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다.")
    public ResponseEntity<ResponseDTO> registerMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        memberService.saveMember(memberRequestDto);
        ResponseDTO responseDTO = new ResponseDTO("회원가입이 완료되었습니다.");
        return ResponseEntity.ok(responseDTO);
    }
}