package com.quostomize.quostomize_be.api.member.controller;

import com.quostomize.quostomize_be.api.member.dto.UpdateAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateEmailDTO;
import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdatePhoneNumberDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.service.MemberService;
import com.quostomize.quostomize_be.api.member.dto.UpdatePasswordRequest;
import com.quostomize.quostomize_be.api.member.dto.UpdatePhoneNumberRequest;
import com.quostomize.quostomize_be.common.jwt.JwtTokenProvider;
import com.quostomize.quostomize_be.domain.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PatchMapping("/update/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal Long memberId,
                                               @RequestBody @Valid UpdatePasswordRequest request) {
        memberService.updatePassword(memberId, request.password());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "멤버 전화번호 수정 API")
    @PatchMapping("/phone-number")
    public ResponseEntity<Void> updatePhoneNumber(@AuthenticationPrincipal Long memberId,
                                                  @RequestBody @Valid UpdatePhoneNumberRequest request) {
        memberService.updatePhoneNumber(memberId, request.phoneNumber());
        return ResponseEntity.noContent().build();
    }

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
    public ResponseEntity<Void> updateMemberAddress(
            @PathVariable("memberId")
            Long memberId,

            @RequestBody
            UpdateAddressDTO updateAddressDTO
    ) {
        memberService.updateMemberAddress(memberId, updateAddressDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-email/{memberId}")
    @Operation(summary = "회원 이메일 변경", description = "회원의 이메일 정보를 변경합니다.")
    public ResponseEntity<Void> updateMemberEmail(
            @PathVariable("memberId")
            Long memberId,

            @RequestBody
            UpdateEmailDTO updateEmailDTO
    ) {
        memberService.updateMemberEmail(memberId, updateEmailDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-phonenumber/{memberId}")
    @Operation(summary = "회원 이메일 변경", description = "회원의 이메일 정보를 변경합니다.")
    public ResponseEntity<Void> updateMemberPhoneNumber(
            @PathVariable("memberId")
            Long memberId,

            @RequestBody
            UpdatePhoneNumberDTO updatePhoneNumberDTO
    ) {
        memberService.updateMemberPhoneNumber(memberId, updatePhoneNumberDTO);
        return ResponseEntity.noContent().build();
    }


}
