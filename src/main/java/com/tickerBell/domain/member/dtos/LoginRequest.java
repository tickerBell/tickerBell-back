package com.tickerBell.domain.member.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty
    @Size(min = 2)
    @Schema(description = "사용자 Id", example = "username")
    private String username;
    @NotEmpty
    @Size(min = 6)
    @Schema(description = "사용자 비밀번호", example = "pass12!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "비밀번호는 숫자와 영문자를 모두 포함해야 합니다.")
    private String password;
}
