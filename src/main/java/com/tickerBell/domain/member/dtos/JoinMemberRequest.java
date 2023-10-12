package com.tickerBell.domain.member.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinMemberRequest {

    @NotEmpty
    @Size(min = 2)
    @Schema(description = "로그인 ID", example = "abcdefg")
    private String username;
    @NotEmpty
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "비밀번호는 숫자와 영문자를 모두 포함해야 합니다.")
    @Schema(description = "비밀번호", example = "abcdefg1")
    private String password;
    @Schema(description = "핸드폰 번호", example = "01012345678")
    private String phone;
    @Schema(description = "이벤트 등록자: true | 예매자: false", example = "true")
    private Boolean isRegistration;
    @Schema(description = "Oauth 회원가입: true | 일반 회원가입: false", example = "true")
    private Boolean isKakaoJoin;
}
