package com.tickerBell.domain.sms.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SendSmsResponse {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}
