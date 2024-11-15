package com.quostomize.quostomize_be.domain.customizer.member.service;

import com.quostomize.quostomize_be.api.member.dto.UpdateAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateEmailDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdatePhoneNumberDTO;
import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDTO findByMemberId(Long memberId) {
        return MemberResponseDTO.fromEntity(memberRepository.findByMemberId(memberId)
                        .orElseThrow(EntityNotFoundException::new)
                );
    }

    public List<MemberResponseDTO> findAll() {
        return memberRepository.findAll().stream().map(MemberResponseDTO::fromEntity).toList();
    }

    @Transactional
    public MemberResponseDTO updateMemberAddress(Long memberId, UpdateAddressDTO updateAddressDTO) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 엔티티입니다."));

        member.updateAddress(updateAddressDTO.newAddress());
        member.updateDetailAddress(updateAddressDTO.newDetailAddress());

        return MemberResponseDTO.fromEntity(member);
    }

    @Transactional
    public MemberResponseDTO updateMemberEmail(Long memberId, UpdateEmailDTO updateEmailDTO) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 엔티티입니다."));
        member.updateEmail(updateEmailDTO.newEmail());
        return MemberResponseDTO.fromEntity(member);
    }

    @Transactional
    public MemberResponseDTO updateMemberPhoneNumber(Long memberId, UpdatePhoneNumberDTO updatePhoneNumberDTO) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 엔티티입니다."));
        member.updatePhoneNumber(updatePhoneNumberDTO.newPhoneNumber());
        return MemberResponseDTO.fromEntity(member);
    }
}
