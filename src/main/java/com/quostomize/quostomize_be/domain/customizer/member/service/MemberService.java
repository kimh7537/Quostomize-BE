package com.quostomize.quostomize_be.domain.customizer.member.service;

import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
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

    public ResponseDTO<MemberResponseDTO> findByMemberId(String memberId) {
        return new ResponseDTO<>(
                MemberResponseDTO.fromEntity(memberRepository.findByMemberId(Long.parseLong(memberId))
                        .orElseThrow(EntityNotFoundException::new)
                ));
    }

    public ResponseDTO<List<MemberResponseDTO>> findAll() {
        return new ResponseDTO<>(
                memberRepository.findAll().stream().map(MemberResponseDTO::fromEntity).toList());
    }
}
