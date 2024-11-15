package com.quostomize.quostomize_be.domain.customizer.member.service;

import com.quostomize.quostomize_be.api.member.dto.ChangeAddressDTO;
import com.quostomize.quostomize_be.api.member.dto.ChangeEmailDTO;
import com.quostomize.quostomize_be.api.member.dto.MemberResponseDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.member.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.member.repository.MemberRepository;

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
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 단일 조회")
    void getMemberInfoByIdTest() {

        // given
        long memberId = 2L;

        Member savedMember = new Member(2L, "USER", "김철수", "cheolsu0708@testmail.com","cheolsu0708", "00000000", "1234561234567", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279");


        // stub
        given(memberRepository.findByMemberId(memberId)).willReturn(Optional.of(savedMember));

        // when
        ResponseDTO<MemberResponseDTO> findMember = memberService.findByMemberId(memberId);

        // then
        assertThat(findMember.getData().memberName()).isEqualTo(savedMember.getMemberName());
        assertThat(findMember.getData().memberEmail()).isEqualTo(savedMember.getMemberEmail());
        assertThat(findMember.getData().memberPhoneNumber()).isEqualTo(savedMember.getMemberPhoneNumber());

    }

    @Test
    @DisplayName("회원 전체 조회")
    void getAllMemberInfosTest() {

        // given
        List<Member> memberList = new ArrayList<>();
        memberList.add(new Member(1L, "USER", "김철수", "cheolsu0708@testmail.com","cheolsu0708", "00000000", "1201041234567", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279"));
        memberList.add(new Member(2L, "USER", "이돌쇠", "stoneiron1228@testmail.com","stoneiron1228", "00000000", "1311082134567", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279"));
        memberList.add(new Member(3L, "ADMIN", "박춘삼", "springthree0605@testmail.com","springthree0605", "00000000", "1009071234568", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279"));

        List<Member> savedMemberList = new ArrayList<>(memberList);

        // stub
        given(memberRepository.findAll()).willReturn(savedMemberList);

        // when
        ResponseDTO<List<MemberResponseDTO>> resultList = memberService.findAll();

        // then
        assertThat(resultList.getData().size()).isEqualTo(3);

    }


    @Test
    @DisplayName("회원 주소 변경")
    void updateMemberAddressTest() {
        // given
        long memberId = 1L;
        Member member = new Member(memberId, "ADMIN", "박춘삼", "springthree0605@testmail.com","springthree0605", "00000000", "1009071234568", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279");

        String newAddress = "서울시 강남구 테헤란로 123";
        String newDetailAddress = "극비빌딩 B609호";
        ChangeAddressDTO changeAddressDTO = new ChangeAddressDTO(newAddress, newDetailAddress);

        given(memberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(member));

        // when
        ResponseDTO<Void> result = memberService.updateMemberAddress(memberId, changeAddressDTO);

        // then
        assertThat(member.getMemberAddress()).isEqualTo(newAddress);
        assertThat(member.getMemberDetailAddress()).isEqualTo(newDetailAddress);
        assertThat(result).isNotNull();

        verify(memberRepository).findByMemberId(memberId);
    }

    @Test
    @DisplayName("회원 이메일 변경")
    void updateMemberEmailTest() {
        // given
        long memberId = 1L;
        Member member = new Member(memberId, "ADMIN", "박춘삼", "springthree0605@testmail.com","springthree0605", "00000000", "1009071234568", "22220", "개발특별시 자바구 스프링로17길", "부트빌딩 8층 테스트호", "01012345678", "72782279");

        String newEmail = "updateTest03333@testmail.com";
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO(newEmail);


        given(memberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(member));

        // when
        ResponseDTO<Void> result = memberService.updateMemberEmail(memberId, changeEmailDTO);

        // then
        assertThat(member.getMemberEmail()).isEqualTo(newEmail);
        assertThat(result).isNotNull();

        verify(memberRepository).findByMemberId(memberId);
    }





}
