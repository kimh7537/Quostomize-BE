package com.quostomize.quostomize_be.config;


import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class CustomSecurityContextFactory implements WithSecurityContextFactory<MockUser> {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EncryptService encryptService;

    @Override
    public SecurityContext createSecurityContext(MockUser annotation) {
        var member = Member.builder()
                .memberName(annotation.memberName())
                .memberLoginId(annotation.memberLoginId())
                .memberPassword(bCryptPasswordEncoder.encode(annotation.memberPassword()))
                .memberEmail(annotation.memberEmail())
                .residenceNumber(encryptService.encryptResidenceNumber(annotation.residenceNumber()))
                .zipCode(annotation.zipCode())
                .memberAddress(annotation.memberAddress())
                .memberDetailAddress(annotation.memberDetailAddress())
                .memberPhoneNumber(encryptService.encryptPhoneNumber(annotation.memberPhoneNumber()))
                .secondaryAuthCode(encryptService.decryptSecondaryAuthCode(annotation.secodaryAuthCode()))
                .build();

        Member saved = memberRepository.save(member);

        var role = new SimpleGrantedAuthority("ROLE_MEMBER");
        var authenticationToken = new UsernamePasswordAuthenticationToken(saved.getMemberId(),
                "",
                List.of(role));

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
