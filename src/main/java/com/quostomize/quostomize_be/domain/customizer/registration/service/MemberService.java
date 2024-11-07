package com.quostomize.quostomize_be.domain.customizer.registration.service;

import com.quostomize.quostomize_be.api.registration.dto.MemberFormDto;
import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.registration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;


    private Member createMember(MemberFormDto memberFormDto) {
        Member member = new Member();
        member.setMemberName(memberFormDto.getName());
        member.setMemberEmail(memberFormDto.getEmail());
        member.setMemberLoginId(memberFormDto.getMemberLoginId());
        member.setMemberPassword(memberFormDto.getMemberPassword()); // 비밀번호 해시 처리 passwordEncoder.encode(memberFormDto.getMemberPassword())
        member.setZipCode(memberFormDto.getZipCode());
        member.setMemberAddress(memberFormDto.getMemberAddress());
        member.setMemberDetailAddress(memberFormDto.getMemberDetailAddress());
        member.setMemberPhoneNumber(memberFormDto.getMemberPhoneNumber());
        return member;
    }

    public void saveMember (MemberFormDto memberFormDto){
        Member member = createMember(memberFormDto);
        memberRepository.save(member);
    }
}
