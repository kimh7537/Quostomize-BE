package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateEmailDTO;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.auth.component.MemberReader;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public MemberResponseDTO findByMemberId(Long memberId) {
        Member findMember = memberReader.findById(memberId);
        String decryptedPhoneNumber = encryptService.decryptPhoneNumber(findMember.getMemberPhoneNumber());
        return MemberResponseDTO.fromEntityWithDecodedPhoneNumber(findMember, decryptedPhoneNumber);
    }

    public List<MemberResponseDTO> findAll() {
        return memberRepository.findAll().stream().map(MemberResponseDTO::fromEntity).toList();
    }

    public Page<Member> getPagedMembers(Pageable pageable) {return memberRepository.findAll(pageable);}

    public Page<Member> getRoleMembers(Pageable pageable, MemberRole role) {return memberRepository.findByRole(pageable, role);}

    @Transactional
    public void updateMemberAddress(Long memberId, UpdateAddressDTO updateAddressDTO) {
        Member member =  memberReader.findById(memberId);

        member.updateZipCode(updateAddressDTO.zipCode());
        member.updateAddress(updateAddressDTO.newAddress());
        member.updateDetailAddress(updateAddressDTO.newDetailAddress());
    }

    @Transactional
    public void updateMemberEmail(Long memberId, String newEmail) {
        Member member = memberReader.findById(memberId);
        member.updateEmail(newEmail);
    }


    @Transactional
    public void updatePhoneNumber(Long memberId, String phoneNumber) {
        Member findMember = memberReader.findById(memberId);
        String encryptedPhoneNumber = encryptService.encryptPhoneNumber(phoneNumber);
        findMember.updatePhoneNumber(encryptedPhoneNumber);
    }

}