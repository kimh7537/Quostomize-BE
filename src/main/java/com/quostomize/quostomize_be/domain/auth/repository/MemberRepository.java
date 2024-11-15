package com.quostomize.quostomize_be.domain.auth.repository;

import com.quostomize.quostomize_be.domain.auth.entity.Member;
import com.quostomize.quostomize_be.domain.auth.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberLoginId(String loginId);

    Optional<Member> findByMemberEmail(String email);

    Optional<Member> findByMemberPhoneNumber(String phone);

    Optional<Member> findByResidenceNumber(String phone);

    List<Member> findAllByRole(MemberRole memberRole);

    boolean existsByMemberLoginId(String loginId);

    boolean existsByMemberPhoneNumber(String phone);

    boolean existsByMemberEmail(String email);

    @Transactional
    @Modifying
    @Query("select m from Member m where m.role in :roles")
    List<Member> findAllByRoleInQuery(@Param("roles") List<MemberRole> roles);
}
