package com.tickerBell.domain.member.dtos.join;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinSmsValidationResponse {
    private Integer randomCode;
}
