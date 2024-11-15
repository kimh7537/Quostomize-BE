package com.quostomize.quostomize_be.domain.customizer.member.service;

import com.quostomize.quostomize_be.api.member.dto.ChangeAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
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

    public ResponseDTO<MemberResponseDTO> findByMemberId(Long memberId) {
        return new ResponseDTO<>(
                MemberResponseDTO.fromEntity(memberRepository.findByMemberId(memberId)
                        .orElseThrow(EntityNotFoundException::new)
                ));
    }

    public ResponseDTO<List<MemberResponseDTO>> findAll() {
        return new ResponseDTO<>(
                memberRepository.findAll().stream().map(MemberResponseDTO::fromEntity).toList());
    }

    @Transactional
    public ResponseDTO<Void> updateMemberAddress(Long memberId, ChangeAddressDTO changeAddressDTO) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 엔티티입니다."));

        member.changeAddress(changeAddressDTO.newAddress());
        member.changeDetailAddress(changeAddressDTO.newDetailAddress());

        return new ResponseDTO<>(null);
    }
}
