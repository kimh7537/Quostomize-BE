package com.quostomize.quostomize_be.api.member.controller;

import com.quostomize.quostomize_be.api.member.dto.UpdateAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateEmailDTO;
import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdatePasswordRequest;
import com.quostomize.quostomize_be.api.member.dto.UpdatePhoneNumberRequest;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.common.jwt.JwtTokenProvider;
import com.quostomize.quostomize_be.domain.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/member")
@RequiredArgsConstructor
@Tag(name = "회원 관련 API", description = "회원가입 한 사용자 정보를 조회 및 변경할 수 있음")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    //로그인 하지 않은상태에서도 비밀번호 변경을 사용할 수 있음
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/update/password")
    @Operation(summary = "비밀번호 변경", description = "비밀번호를 업데이트 합니다.")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal Long memberId,
                                               @RequestBody @Valid UpdatePasswordRequest request) {
        memberService.updatePassword(memberId, request.password());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    @Operation(summary = "단일 회원 정보 조회", description = "유저의 memberId로 회원 정보를 조회합니다.")
    public ResponseEntity<ResponseDTO<MemberResponseDTO>> findByMemberId(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(new ResponseDTO<>(memberService.findByMemberId(memberId)));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "전체 회원 정보 조회", description = "전체 회원 정보를 조회합니다.")
    public ResponseEntity<ResponseDTO<List<MemberResponseDTO>>> findAll() {
        return ResponseEntity.ok(new ResponseDTO<>(memberService.findAll()));
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/change-phone")
    @Operation(summary = "멤버 전화번호 수정 API", description = "회원의 전화번호 정보를 변경합니다.")
    public ResponseEntity<ResponseDTO<String>> updatePhoneNumber(@AuthenticationPrincipal Long memberId,
                                                  @RequestBody @Valid UpdatePhoneNumberRequest request) {
        memberService.updatePhoneNumber(memberId, request.phoneNumber());
        return ResponseEntity.ok(new ResponseDTO<>(request.phoneNumber()));
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/change-address")
    @Operation(summary = "회원 주소 변경", description = "회원의 자택 주소 정보를 변경합니다.")
    public ResponseEntity<UpdateAddressDTO> updateMemberAddress(
            @AuthenticationPrincipal Long memberId,
            @RequestBody UpdateAddressDTO updateAddressDTO
    ) {
        memberService.updateMemberAddress(memberId, updateAddressDTO);
        return ResponseEntity.ok(updateAddressDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/change-email")
    @Operation(summary = "회원 이메일 변경", description = "회원의 이메일 정보를 변경합니다.")
    public ResponseEntity<ResponseDTO<String>> updateMemberEmail(
            @AuthenticationPrincipal Long memberId,
            @RequestBody UpdateEmailDTO updateEmailDTO
    ) {
        memberService.updateMemberEmail(memberId, updateEmailDTO.newEmail());
        return ResponseEntity.ok(new ResponseDTO<>(updateEmailDTO.newEmail()));
    }
    
}
