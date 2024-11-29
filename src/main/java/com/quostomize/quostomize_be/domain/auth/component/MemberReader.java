package com.quostomize.quostomize_be.domain.auth.component;


import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.repository.MemberQueryDslRepository;
import com.quostomize.quostomize_be.domain.auth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReader {

    private final MemberRepository memberRepository;
    private final MemberQueryDslRepository memberQueryDslRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    public Member findByMemberIdWithLock(Long memberId) {
        return memberQueryDslRepository.findByMemberIdWithLock(memberId);
    }

}
