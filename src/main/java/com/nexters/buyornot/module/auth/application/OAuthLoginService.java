package com.nexters.buyornot.module.auth.application;

import com.nexters.buyornot.module.auth.model.AuthTokens;
import com.nexters.buyornot.module.auth.model.AuthTokensGenerator;
import com.nexters.buyornot.module.auth.model.oauth.OAuthInfoResponse;
import com.nexters.buyornot.module.auth.model.oauth.OAuthLoginParams;
import com.nexters.buyornot.module.auth.model.oauth.RequestOAuthInfoService;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
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
        UUID memberId = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(memberId);
    }

    private UUID findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private UUID newMember(OAuthInfoResponse oAuthInfoResponse) {
        User user = User.builder()
                .gender(oAuthInfoResponse.getGender())
                .birthday(oAuthInfoResponse.getBirthday())
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .ageRange(oAuthInfoResponse.getAgeRange())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .role(Role.USER)
                .build();

        return userRepository.save(user).getId();
    }
}
