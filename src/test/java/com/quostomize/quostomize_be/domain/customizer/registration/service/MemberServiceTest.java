package com.quostomize.quostomize_be.domain.customizer.registration.service;

import com.quostomize.quostomize_be.api.registration.dto.MemberRequestDto;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.registration.entity.Member;
import com.quostomize.quostomize_be.domain.customizer.registration.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate; //Customer_id 에 test가 영향을 받아 잠시 Fk 비활성화 하기 위해 사용

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clean(){
        memberRepository.deleteAll();

    }

    @BeforeEach
    void setUp(){
        jdbcTemplate.execute("SET foreign_key_checks = 0;"); // 외래 키 제약 조건 비활성화
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("SET foreign_key_checks = 1;"); // 외래 키 제약 조건 활성화
    }


    @Test
    @DisplayName("중복된 이메일로 인한 예외 발생 테스트")
    void saveMember_DuplicateEmail_Exception() {
        // given
        Member member = Member.builder()
                .memberName("testName")
                .memberEmail("test@example.com")
                .memberLoginId("testLoginId")
                .memberPassword("password123")
                .residenceNumber("1234567891011")
                .zipCode("12345")
                .memberAddress("testAddress")
                .memberDetailAddress("testAddress111")
                .memberPhoneNumber("01012345645")
                .secondaryAuthCode("456789")
                .build();

        // 엔티티 저장
        memberRepository.save(member);
        
        
        // 중복된 이메일로 회원가입 시도
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

    @Test
    @DisplayName("유효하지 않은 아이디로 회원가입 시도")
    void notValid_LoginId_Exception(){
        // given
        MemberRequestDto notValidLoginId = new MemberRequestDto(
                "testName2",
                "test@example.com",
                "2312gjkdfs",
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
        AppException exception = assertThrows(AppException.class, () -> memberService.saveMember(notValidLoginId));
        assertEquals("INVALID_LOGIN_ID", exception.getErrorCode().name());

    }

}
