package com.tickerBell.global.security.token;

import com.tickerBell.domain.member.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final Key key;
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 2L; // 2 hours
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 30L; // 30 days

    @Autowired
    public JwtTokenProvider(@Value("${app.auth.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username, Role role) {
        Map<String, Object> claim = new HashMap<>();
        claim.put("username", username); // 사용자 ID
        claim.put("role", role); // 사용자 권한
        return createJwt("ACCESS_TOKEN", ACCESS_TOKEN_EXPIRATION_TIME, claim);
    }

    public String createRefreshToken(String username, Role role) {
        HashMap<String, Object> claim = new HashMap<>();
        claim.put("username", username); // 사용자 ID
        claim.put("role", role); // 사용자 권한
        return createJwt("REFRESH_TOKEN", REFRESH_TOKEN_EXPIRATION_TIME, claim);
    }

    public String createJwt(String subject, Long expiration, Map<String, Object> claim) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(key, SignatureAlgorithm.HS256);

        if (claim != null) {
            jwtBuilder.setClaims(claim);
        }

        // 만료 기한 설정
        if (expiration != null) {
            jwtBuilder.setExpiration(new Date(new Date().getTime() + expiration));
        }

        return jwtBuilder.compact();
    }

    /**
     * 복호화
     */
    public Claims get(String jwt) throws JwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 토큰 만료 여부 체크
     *
     * @return true : 만료됨, false : 만료되지 않음
     */
    public boolean isExpiration(String jwt) throws JwtException {
        try {
            return get(jwt).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Refresh token refresh 여부 확인
     * 만료일 7일 이내 일 경우 refresh token 재발급
     */
    public boolean canRefresh(String refreshToken) throws JwtException {
        Claims claims = get(refreshToken);
        long expirationTime = claims.getExpiration().getTime();
        long weekTime = 1000 * 60 * 60 * 24 * 7L;

        return new Date().getTime() > (expirationTime - weekTime);
    }
}
