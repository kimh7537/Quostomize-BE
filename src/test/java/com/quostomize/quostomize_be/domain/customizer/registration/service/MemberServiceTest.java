package com.quostomize.quostomize_be.domain.customizer.registration.service;

import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.registration.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clean(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입을 하려는 자.")
    void saveMember_Success() {
        // given
        MemberRequestDto requestDto = new MemberRequestDto(
                "testName",
                "test@example.com",
                "testLoginId",
                "",
                "password123",
                "password123",
                "1234567891012",
                "12345",
                "testDetailAddress",
                "testDetailAddress",
                "01022225555" ,
                "123456",
                "123456"
        );

        // when & then
        assertDoesNotThrow(() -> memberService.saveMember(requestDto));
        assertEquals(1, memberRepository.count(), "회원이 저장되지 않았습니다.");
    }

    @Test
    @DisplayName("중복된 이메일로 인한 예외 발생 테스트")
    void saveMember_DuplicateEmail_Exception() {
        // given
        MemberRequestDto requestDto = new MemberRequestDto(
                "testName",
                "test@example.com",
                "testLoginId",
                "",
                "password123",
                "password123",
                "1234567891011",
                "12345",
                "testAddress",
                "testAddress111",
                "01012345678",
                "456789",
                "456789"
        );
        memberService.saveMember(requestDto);

        MemberRequestDto duplicateEmailDto = new MemberRequestDto(
                "testName2",
                "test@example.com",
                "testLoginId2",
                "",
                "password123",
                "password123",
                "1234545678912",
                "12345",
                "testDetailAddress",
                "testDetailAddress",
                "01055558888",
                "123456",
                "123456"
        );

        // when & then
        AppException exception = assertThrows(AppException.class, () -> memberService.saveMember(duplicateEmailDto));
        assertEquals("EMAIL_DUPLICATED", exception.getErrorCode().name());
    }

}
