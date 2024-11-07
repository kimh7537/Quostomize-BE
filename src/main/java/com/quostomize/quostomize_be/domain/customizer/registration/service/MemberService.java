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
public interface MemberService {
    void saveMember(MemberRequestDto memberRequestDto);
    MemberResponseDto getMember(Long id);
}