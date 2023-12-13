package com.nexters.buyornot.global.jwt;

import com.nexters.buyornot.global.common.codes.ErrorCode;
import com.nexters.buyornot.global.common.constant.RedisKey;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private static final String BEARER_TYPE = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 30;  // 30일
    private static final long EXPIRED_TIME = 1L;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtTokenProvider(@Value("${jwt.token.secret-key}") String secretKey, RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public AuthTokens generate(JwtUser jwtUser) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        //access token
        String accessToken = Jwts.builder()
                .setSubject(jwtUser.getId().toString())
                .claim("nickname", jwtUser.getNickname())
                .claim("profile", jwtUser.getProfile())
                .claim("role", jwtUser.getRole())
                .setExpiration(accessTokenExpiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        //refresh token
        String refreshToken = Jwts.builder()
                .setSubject(jwtUser.getId().toString())
                .setExpiration(accessTokenExpiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(refreshTokenExpiredAt)
                .compact();

        redisTemplate.opsForValue()
                .set(RedisKey.REFRESH_TOKEN + jwtUser.getId(), refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        log.info("user: " + jwtUser.getNickname() + " access token: " + accessToken);
        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);

    }

    public void expireRefreshToken(UUID userId) {
        redisTemplate.opsForValue().set(RedisKey.REFRESH_TOKEN + userId, "", EXPIRED_TIME, TimeUnit.MILLISECONDS);
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

    public JwtCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtCode.ACCESS;
        }  catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token", e);
            return JwtCode.EXPIRED;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | DecodingException e) {
            log.warn("Invalid JWT Token", e);
        }
        catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty.", e);
        } catch (Exception e) {
            log.error("Unhandled JWT exception", e);
        }
        return JwtCode.DENIED;
    }

    public JwtUser getJwtUser(String token) {

        if(!token.isEmpty() && token.startsWith("Bearer ")) token = token.substring(7);

        if(!validateToken(token).equals(JwtCode.ACCESS)) {
            throw new BusinessExceptionHandler(ErrorCode.EXPIRED_ACCESS_TOKEN_EXCEPTION);
        }
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
