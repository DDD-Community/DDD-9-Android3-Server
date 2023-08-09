package com.nexters.buyornot.module.auth.model;

import com.nexters.buyornot.global.common.constant.RedisKey;
import com.nexters.buyornot.global.jwt.JwtTokenProvider;
import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

//        long now = (new Date()).getTime();
//        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
//        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
//
//        String accessToken = jwtTokenProvider.generate(jwtUser, accessTokenExpiredAt);
//        String refreshToken = jwtTokenProvider.generate(jwtUser, refreshTokenExpiredAt);
//
//        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    public UUID extractMemberId(String accessToken) {
        return UUID.fromString(jwtTokenProvider.extractSubject(accessToken));
    }
}
