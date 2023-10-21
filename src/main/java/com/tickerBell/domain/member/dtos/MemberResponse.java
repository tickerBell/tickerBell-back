package com.tickerBell.domain.member.dtos;

import com.tickerBell.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long memberId;
    private String username;
    private String phone;
    private Role role;
    private Boolean isAdult;
}
