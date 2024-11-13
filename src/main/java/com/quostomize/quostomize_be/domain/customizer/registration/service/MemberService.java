package com.quostomize.quostomize_be.domain.customizer.registration.service;


import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.api.registration.dto.MemberResponseDto;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.registration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    public void validateDuplicateMember(MemberRequestDto RequestDto) {
        // 이메일 중복 확인
        Member findMemberByEmail = memberRepository.findByMemberEmail(RequestDto.memberEmail());
        if (findMemberByEmail != null) {
            throw new AppException(ErrorCode.EMAIL_DUPLICATED);
        }

        // ID 중복 확인
        Member findMemberByLoginId = memberRepository.findByMemberLoginId(RequestDto.memberLoginId());
        if (findMemberByLoginId != null) {
            throw new AppException(ErrorCode.MEMBER_LOGIN_ID_DUPLICATED);
        }

        // 전화번호 중복 확인
        Member findMemberByPhoneNumber = memberRepository.findByMemberPhoneNumber(RequestDto.memberPhoneNumber());
        if (findMemberByPhoneNumber != null) {
            throw new AppException(ErrorCode.PHONE_NUMBER_DUPLICATED);
        }
    }

    // 비밀번호 확인 메서드
    public void validatePassword(MemberRequestDto requestDto) {
        if (!requestDto.isValidPassword()) {
            throw new AppException(ErrorCode.NOT_MATCHED_PASSWORD);
        }
    }

    // 2차 인증번호 확인 메서드
    public void validateSecondaryAuthCode(MemberRequestDto requestDto) {
        if (!requestDto.isValidSecondaryAuthCode()) {
            throw new AppException(ErrorCode.NOT_MATCHED_SECONDARY_AUTH_CODE);
        }
    }

    private Member createMember(MemberRequestDto memberRequestDto) {
        return Member.builder()
                .memberName(memberRequestDto.memberName())
                .memberEmail(memberRequestDto.memberEmail())
                .memberLoginId(memberRequestDto.memberLoginId())
                .memberPassword(memberRequestDto.memberPassword()) // 비밀번호 해시 처리 필요 시 passwordEncoder.encode(memberRequestDto.memberPassword())
                .residenceNumber(memberRequestDto.residenceNumber())
                .zipCode(memberRequestDto.zipCode())
                .memberAddress(memberRequestDto.memberAddress())
                .memberDetailAddress(memberRequestDto.memberDetailAddress())
                .memberPhoneNumber(memberRequestDto.memberPhoneNumber())
                .secondaryAuthCode(memberRequestDto.secondaryAuthCode())
                .build();
    }

    public void saveMember (MemberRequestDto memberRequestDto) {
        validateDuplicateMember(memberRequestDto);

        if (!memberRequestDto.isValidPassword()) {
            throw new AppException(ErrorCode.NOT_MATCHED_PASSWORD);
        }

        if (!memberRequestDto.isValidSecondaryAuthCode()) {
            throw new AppException(ErrorCode.NOT_MATCHED_SECONDARY_AUTH_CODE);
        }

        if (!MemberRequestDto.isValidPhoneNumber(memberRequestDto.memberPhoneNumber())) {
            throw new AppException(ErrorCode.INVALID_PHONE_NUMBER);
        }

        if (!MemberRequestDto.isValidEmail(memberRequestDto.memberEmail())) {
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }

        Member member = createMember(memberRequestDto);
        memberRepository.save(member);
    }


    public MemberResponseDto memberResponseDto(Long id) {

        // 멤버 조회
        return null;
    }
}
