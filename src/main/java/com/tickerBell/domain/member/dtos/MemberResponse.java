package com.tickerBell.domain.member.dtos;

import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponse {

    private Long memberId;
    private String username;
    private String phone;
    private Role role;
    private Boolean isAdult;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .phone(member.getPhone())
                .role(member.getRole())
                .isAdult(member.getIsAdult())
                .build();
    }
}
