package com.tickerBell.domain.member.entity;

import com.tickerBell.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NonMember extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "non_member_id")
    private Long id;

    private String name; // 비회원 예매 시 입력 이름
    private String phone; // 비회원 예매 시 입력 휴대폰 번호
}
