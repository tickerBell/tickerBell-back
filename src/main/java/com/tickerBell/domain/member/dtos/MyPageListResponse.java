package com.tickerBell.domain.member.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageListResponse {

    // 회원 관련
    private String username;
    private String phone;
    private Boolean isRegistrant; // 등록자 여부

    // 페이징 총 개수
    private Long totalCount;

    private List<MyPageResponse> myPageResponse;
}
