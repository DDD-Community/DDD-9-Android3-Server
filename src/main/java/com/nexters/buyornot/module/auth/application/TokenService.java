package com.nexters.buyornot.module.auth.application;

import com.nexters.buyornot.global.common.codes.ErrorCode;
import com.nexters.buyornot.global.common.constant.RedisKey;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.global.jwt.JwtTokenProvider;
import com.nexters.buyornot.module.auth.api.dto.request.ReissueTokenReq;
import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthTokens reissueToken(ReissueTokenReq request) {
        UUID userId = jwtTokenProvider.getUserId(request.getAccessToken());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.UNAUTHORIZED_USER_EXCEPTION));

        if(!jwtTokenProvider.validateToken(request.getRefreshToken()).equals(JwtTokenProvider.JwtCode.ACCESS)) {
            throw new BusinessExceptionHandler(ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
        }

        String refreshToken = (String) redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN + userId);

        if(Objects.isNull(refreshToken)) {
            throw new BusinessExceptionHandler(ErrorCode.EXPIRED_REFRESH_TOKEN_EXCEPTION);
        }

        if (!refreshToken.equals(request.getRefreshToken())) {
            jwtTokenProvider.expireRefreshToken(user.getId());
            throw new BusinessExceptionHandler(ErrorCode.INCONSISTENT_REFRESH_TOKEN_EXCEPTION);
        }

        AuthTokens tokens = jwtTokenProvider.generate(user.toJwtUser());
        redisTemplate.delete(refreshToken);
        return tokens;
    }

}
