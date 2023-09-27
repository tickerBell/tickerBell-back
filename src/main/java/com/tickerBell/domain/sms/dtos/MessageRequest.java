package com.tickerBell.domain.sms.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageRequest {
    String to;
    String content;
}
