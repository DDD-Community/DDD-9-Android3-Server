package com.nexters.buyornot.global.jwt;

import com.nexters.buyornot.module.user.dto.JwtUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.token.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(JwtUser jwtUser, Date expiredAt) {
        return Jwts.builder()
                .setSubject(jwtUser.getId().toString())
                .claim("name", jwtUser.getName())
                .claim("email", jwtUser.getEmail())
                .claim("nickname", jwtUser.getNickname())
                .claim("role", jwtUser.getRole())
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    private Claims parseClaims(String accessToken) {

        if(!accessToken.isEmpty() && accessToken.startsWith("Bearer ")) accessToken = accessToken.substring(7);

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public JwtUser getJwtUser(String token) {
        Claims claims = parseClaims(token);
        return JwtUser.newJwtUser(claims);
    }

    public UUID getUserId(String token) {
        return UUID.fromString(extractSubject(token));
    }

    public static enum JwtCode {
        DENIED,
        ACCESS,
        EXPIRED;
    }
}
