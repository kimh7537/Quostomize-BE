package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateEmailDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdatePhoneNumberDTO;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.component.MemberReader;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberReader memberReader;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EncryptService encryptService;
    private final ValidateService validateService;

    @Transactional
    public void updatePassword(final Long memberId, final String password) {
        Member findMember = memberReader.findById(memberId);

        validateService.checkPasswordPattern(password); //패스워드 규칙에 맞는지 확인
        validateIsSameBefore(findMember.getMemberPassword(), password); //패스워드를 암호화 한 후 비교

        findMember.updatePassword(bCryptPasswordEncoder.encode(password));
    }

    private void validateIsSameBefore(String originPassword, String newPassword) {
        if (bCryptPasswordEncoder.matches(newPassword, originPassword)) {
            throw new AppException(ErrorCode.SAME_PASSWORD);
        }
    }

    @Transactional
    public void updatePhoneNumber(Long memberId, String phoneNumber) {
        Member findMember = memberReader.findById(memberId);
        String encryptedPhoneNumber = encryptService.encryptPhoneNumber(phoneNumber);
        findMember.updatePhoneNumber(encryptedPhoneNumber);
    }

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

        member.updateZipCode(updateAddressDTO.zipCode());
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