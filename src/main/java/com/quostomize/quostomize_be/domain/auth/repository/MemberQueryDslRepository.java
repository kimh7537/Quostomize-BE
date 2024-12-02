package com.quostomize.quostomize_be.domain.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.entity.QMember;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public Member findByMemberIdWithLock(Long memberId) {
        return queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.memberId.eq(memberId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
