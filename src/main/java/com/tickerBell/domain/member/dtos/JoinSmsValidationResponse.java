package com.tickerBell.domain.member.dtos;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinSmsValidationResponse {
    private Integer randomCode;
}
