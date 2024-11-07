package com.quostomize.quostomize_be.domain.customizer.registration.service;

import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.api.registration.dto.MemberResponseDto;

public interface MemberService {
    void saveMember(MemberRequestDto memberRequestDto);
    MemberResponseDto getMember(Long id);
}