package com.tickerBell.domain.member.repository;

import com.tickerBell.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username); // 사용자 Id를 이용해 회원객체 조회
}
