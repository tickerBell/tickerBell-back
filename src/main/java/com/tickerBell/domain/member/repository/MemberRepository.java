package com.tickerBell.domain.member.repository;

import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username); // 사용자 Id를 이용해 회원객체 조회
    boolean existsByIdAndAuthProvider(String id, AuthProvider authProvider);

}
