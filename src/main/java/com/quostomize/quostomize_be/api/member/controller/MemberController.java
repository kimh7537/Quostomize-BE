package com.quostomize.quostomize_be.api.member.controller;

import com.quostomize.quostomize_be.api.member.dto.ChangeAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.ChangeEmailDTO;
import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    @Operation(summary = "단일 회원 정보 조회", description = "유저의 memberId를 pathVariable로 해서 회원 정보를 조회합니다.")
    public ResponseEntity<ResponseDTO<MemberResponseDTO>> findByMemberId(
            @PathVariable("memberId")
            Long memberId
    ) {
        return ResponseEntity.ok(new ResponseDTO<>(memberService.findByMemberId(memberId)));
    }

    @GetMapping
    @Operation(summary = "전체 회원 정보 조회", description = "전체 회원 정보를 조회합니다.")
    public ResponseEntity<ResponseDTO<List<MemberResponseDTO>>> findAll() {
        return ResponseEntity.ok(new ResponseDTO<>(memberService.findAll()));
    }

    @PatchMapping("/change-address/{memberId}")
    @Operation(summary = "회원 주소 변경", description = "회원의 자택 주소 정보를 변경합니다.")
    public ResponseEntity<ResponseDTO<Void>> updateMemberAddress(
            @PathVariable("memberId")
            Long memberId,

            @RequestBody
            ChangeAddressDTO changeAddressDTO
    ) {
        memberService.updateMemberAddress(memberId, changeAddressDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-email/{memberId}")
    @Operation(summary = "회원 이메일 변경", description = "회원의 이메일 정보를 변경합니다.")
    public ResponseEntity<ResponseDTO<Void>> updateMemberEmail(
            @PathVariable("memberId")
            Long memberId,

            @RequestBody
            ChangeEmailDTO changeEmailDTO
    ) {
        memberService.updateMemberEmail(memberId, changeEmailDTO);
        return ResponseEntity.ok().build();
    }

}
