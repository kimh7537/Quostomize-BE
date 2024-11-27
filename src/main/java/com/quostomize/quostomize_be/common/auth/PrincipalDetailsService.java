package com.quostomize.quostomize_be.common.auth;

import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByMemberLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 회원이 존재하지 않습니다."));
        log.info("해당 유저의 로그인 요청: {}", findMember.getMemberLoginId());
        return new PrincipalDetails(findMember);
    }
}
