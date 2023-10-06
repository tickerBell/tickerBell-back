package com.tickerBell.domain.member.repository;

import com.tickerBell.domain.member.entity.NonMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NonMemberRepository extends JpaRepository<NonMember, Long> {
    @Query("select nm from NonMember nm where nm.name = :name and nm.phone = :phone")
    Optional<NonMember> findByNameAndPhone(@Param("name") String name,@Param("phone") String phone);
}
