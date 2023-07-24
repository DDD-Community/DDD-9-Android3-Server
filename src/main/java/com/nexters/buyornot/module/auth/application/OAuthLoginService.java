package com.nexters.buyornot.module.auth.application;

import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.auth.model.AuthTokensGenerator;
import com.nexters.buyornot.module.auth.model.oauth.OAuthInfoResponse;
import com.nexters.buyornot.module.auth.model.oauth.OAuthLoginParams;
import com.nexters.buyornot.module.auth.model.oauth.RequestOAuthInfoService;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        JwtUser user = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(user);
    }

    private JwtUser findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::toJwtUser)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private JwtUser newMember(OAuthInfoResponse oAuthInfoResponse) {
        User user = User.builder()
                .gender(oAuthInfoResponse.getGender())
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .ageRange(oAuthInfoResponse.getAgeRange())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.toJwtUser();
    }
}
