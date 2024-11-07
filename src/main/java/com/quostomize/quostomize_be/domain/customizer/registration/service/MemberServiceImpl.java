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
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    public void validateDuplicateMember(MemberRequestDto memberRequestDto) {
        // 이메일 중복 확인
        Member findMemberByEmail = memberRepository.findByMemberEmail(memberRequestDto.memberEmail());
        if (findMemberByEmail != null) {
            throw new AppException(ErrorCode.EMAIL_DUPLICATED);
        }

        // ID 중복 확인
        Member findMemberByLoginId = memberRepository.findByMemberLoginId(memberRequestDto.memberLoginId());
        if (findMemberByLoginId != null) {
            throw new AppException(ErrorCode.MEMBER_LOGIN_ID_DUPLICATED);
        }

        // 전화번호 중복 확인
        Member findMemberByPhoneNumber = memberRepository.findByMemberPhoneNumber(memberRequestDto.memberPhoneNumber());
        if (findMemberByPhoneNumber != null) {
            throw new AppException(ErrorCode.PHONE_NUMBER_DUPLICATED);
        }
    }

    // 비밀번호 확인 메서드
    public void validatePassword(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new AppException(ErrorCode.NOT_MATCHED_PASSWORD);
        }
    }

    // 2차 인증번호 확인 메서드
    public void validateSecondaryAuthCode(String secondaryAuthCode, String secondaryAuthCodeConfirm) {
        if (!secondaryAuthCode.equals(secondaryAuthCodeConfirm)) {
            throw new AppException(ErrorCode.NOT_MATCHED_SECONDARY_AUTH_CODE);
        }
    }

    private Member createMember(MemberRequestDto memberRequestDto) {
        Member member = new Member();
        member.setMemberName(memberRequestDto.memberName());
        member.setMemberEmail(memberRequestDto.memberEmail());
        member.setMemberLoginId(memberRequestDto.memberLoginId());
        member.setMemberPassword(memberRequestDto.memberPassword()); // 비밀번호 해시 처리 passwordEncoder.encode(memberFormDto.getMemberPassword())
        member.setResidenceNumber(memberRequestDto.residenceNumber());
        member.setZipCode(memberRequestDto.zipCode());
        member.setMemberAddress(memberRequestDto.memberAddress());
        member.setMemberDetailAddress(memberRequestDto.memberDetailAddress());
        member.setMemberPhoneNumber(memberRequestDto.memberPhoneNumber());
        member.setSecondaryAuthCode(memberRequestDto.secondaryAuthCode());
        return member;
    }


    @Override
    public void saveMember(MemberRequestDto memberRequestDto) {
        validateDuplicateMember(memberRequestDto);
        validatePassword(memberRequestDto.memberPassword(), memberRequestDto.memberPasswordConfirm());
        validateSecondaryAuthCode(memberRequestDto.secondaryAuthCode(), memberRequestDto.secondaryAuthCodeConfirm());
        Member member = createMember(memberRequestDto);
        memberRepository.save(member);
    }

    @Override
    public MemberResponseDto getMember(Long id) {
//        Member member = memberRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Member not found"));
//        return new MemberResponseDto(member.getMemberLoginId(), member.getMemberName(), member.getMemberEmail(), member.getMemberPhoneNumber());
        return null;
    }
}
