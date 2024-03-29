package com.nexters.buyornot.module.auth.model.oauth;

import com.nexters.buyornot.module.auth.api.dto.request.KakaoLoginParams;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService {
    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
        );
    }

    public OAuthInfoResponse request(KakaoLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());
//        String accessToken = client.requestAccessToken(params);
        String accessToken = params.getAccessToken();
        return client.requestOauthInfo(accessToken);
    }
}
