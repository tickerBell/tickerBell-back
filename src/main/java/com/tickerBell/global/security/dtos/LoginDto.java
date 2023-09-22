package com.tickerBell.global.security.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {

    @NotEmpty
    @Size(min = 2)
    private String username;
    @NotEmpty
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "비밀번호는 숫자와 영문자를 모두 포함해야 합니다.")
    private String password;
}
