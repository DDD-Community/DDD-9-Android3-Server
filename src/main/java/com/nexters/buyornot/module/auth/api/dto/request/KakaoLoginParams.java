package com.nexters.buyornot.module.auth.api.dto.request;

import com.nexters.buyornot.module.auth.model.oauth.OAuthLoginParams;
import com.nexters.buyornot.module.auth.model.oauth.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Getter
@NoArgsConstructor
public class KakaoLoginParams implements OAuthLoginParams {
//    private String authorizationCode;
    private String accessToken;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("code", authorizationCode);
        body.add("code", accessToken);
        log.info("kakao access token: " + accessToken);
        return body;
    }
}
