package com.nexters.buyornot.module.auth.model;

import com.nexters.buyornot.global.jwt.JwtTokenProvider;
import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthTokens generate(JwtUser jwtUser) {
        return jwtTokenProvider.generate(jwtUser);
    }

    public UUID extractMemberId(String accessToken) {
        return UUID.fromString(jwtTokenProvider.extractSubject(accessToken));
    }

    public void expireToken(UUID userId) {
        jwtTokenProvider.expireRefreshToken(userId);
    }
}
