package com.quostomize.quostomize_be.domain.customizer.registration.service;


import com.quostomize.quostomize_be.api.registration.dto.MemberFormDto;
import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.api.registration.dto.MemberResponseDto;
import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.registration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;


    private Member createMember(MemberRequestDto memberRequestDto) {
        Member member = new Member();
        member.setMemberName(memberRequestDto.name());
        member.setMemberEmail(memberRequestDto.email());
        member.setMemberLoginId(memberRequestDto.memberLoginId());
        member.setMemberPassword(memberRequestDto.memberPassword()); // 비밀번호 해시 처리 passwordEncoder.encode(memberFormDto.getMemberPassword())
        member.setZipCode(memberRequestDto.zipCode());
        member.setMemberAddress(memberRequestDto.memberAddress());
        member.setMemberDetailAddress(memberRequestDto.memberDetailAddress());
        member.setMemberPhoneNumber(memberRequestDto.memberPhoneNumber());
        return member;
    }




    @Override
    public void saveMember(MemberRequestDto memberRequestDto) {
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
