package com.quostomize.quostomize_be.domain.auth.service;

import com.quostomize.quostomize_be.api.member.dto.UpdateAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.UpdateEmailDTO;
import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;

import com.quostomize.quostomize_be.api.member.dto.UpdatePhoneNumberRequest;
import com.quostomize.quostomize_be.domain.auth.component.MemberReader;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@Transactional(readOnly = true)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EncryptService encryptService;

    @Mock
    private MemberReader memberReader;

    @Test
    @DisplayName("회원 단일 조회 - 성공")
    void getMemberInfoByIdTest() {
        // given
        long memberId = 2L;
        String decryptedPhoneNumber = "01012345678"; // 복호화된 전화번호
        Member savedMember = new Member(
                "김철수",
                "cheolsu0708@testmail.com",
                "cheolsu0708",
                "00000000",
                "1234561234567",
                "22220",
                "개발특별시 자바구 스프링로17길",
                "부트빌딩 8층 테스트호",
                "01012345678",
                "72782279",
                MemberRole.MEMBER
        );

        // Mock 설정
        given(memberReader.findById(memberId)).willReturn(savedMember); // 멤버 조회 Mock
        given(encryptService.decryptPhoneNumber(savedMember.getMemberPhoneNumber())).willReturn(decryptedPhoneNumber); // 복호화 Mock

        // when
        MemberResponseDTO findMember = memberService.findByMemberId(memberId);

        // then
        assertThat(findMember.memberName()).isEqualTo(savedMember.getMemberName());
        assertThat(findMember.memberEmail()).isEqualTo(savedMember.getMemberEmail());
        assertThat(findMember.memberPhoneNumber()).isEqualTo(decryptedPhoneNumber); // 복호화된 전화번호 비교
    }

    @Test
    @DisplayName("회원 전체 조회")
    void getAllMemberInfosTest() {
        // given
        List<Member> memberList = new ArrayList<>();
        memberList.add(new Member("김철수", "cheolsu0708@testmail.com","cheolsu0708", "00000000", "1201041234567", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279", MemberRole.MEMBER));
        memberList.add(new Member("이돌쇠", "stoneiron1228@testmail.com","stoneiron1228", "00000000", "1311082134567", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279", MemberRole.MEMBER));
        memberList.add(new Member("박춘삼", "springthree0605@testmail.com","springthree0605", "00000000", "1009071234568", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279", MemberRole.MEMBER));

        List<Member> savedMemberList = new ArrayList<>(memberList);

        given(memberRepository.findAll()).willReturn(savedMemberList);

        // when
        List<MemberResponseDTO> resultList = memberService.findAll();

        // then
        assertThat(resultList.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("회원 주소 변경")
    void updateMemberAddressTest() {
        // given
        long memberId = 1L;
        Member member = new Member("박춘삼", "springthree0605@testmail.com","springthree0605", "00000000", "1009071234568", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279", MemberRole.ADMIN);

        String zipCode = "12977";
        String newAddress = "서울시 강남구 테헤란로 123";
        String newDetailAddress = "극비빌딩 B609호";
        UpdateAddressDTO updateAddressDTO = new UpdateAddressDTO(zipCode, newAddress, newDetailAddress);

        given(memberReader.findByMemberIdWithLock(memberId)).willReturn(member);

        // when
        memberService.updateMemberAddress(memberId, updateAddressDTO);

        // then
        assertThat(member.getMemberAddress()).isEqualTo(newAddress);
        assertThat(member.getMemberDetailAddress()).isEqualTo(newDetailAddress);

        verify(memberReader).findByMemberIdWithLock(memberId);
    }

    @Test
    @DisplayName("회원 이메일 변경")
    void updateMemberEmailTest() {
        // given
        long memberId = 1L;
        Member member = new Member("박춘삼", "springthree0605@testmail.com","springthree0605", "00000000", "1009071234568", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279", MemberRole.ADMIN);

        String newEmail = "updateTest03333@testmail.com";
        UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO(newEmail);

        given(memberReader.findByMemberIdWithLock(memberId)).willReturn(member);

        // when
        memberService.updateMemberEmail(memberId, updateEmailDTO.newEmail());

        // then
        assertThat(member.getMemberEmail()).isEqualTo(newEmail);

        verify(memberReader).findByMemberIdWithLock(memberId);
    }

    @Test
    @DisplayName("회원 전화번호 변경")
    void updateMemberPhoneNumberTest() {
        // given
        long memberId = 1L;
        Member member = new Member("박춘삼", "springthree0605@testmail.com","springthree0605", "00000000", "1009071234568", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279", MemberRole.ADMIN);

        String newPhoneNumber = "01087659876";
        UpdatePhoneNumberRequest updatePhoneNumberDTO = new UpdatePhoneNumberRequest(newPhoneNumber);

        given(memberReader.findByMemberIdWithLock(memberId)).willReturn(member);
        given(encryptService.encryptPhoneNumber(newPhoneNumber)).willReturn(newPhoneNumber); // Encryption Mock

        // when
        memberService.updatePhoneNumber(memberId, updatePhoneNumberDTO.phoneNumber());

        // then
        assertThat(member.getMemberPhoneNumber()).isEqualTo(newPhoneNumber);

        verify(memberReader).findByMemberIdWithLock(memberId);
    }



}
