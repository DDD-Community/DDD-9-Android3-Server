package com.nexters.buyornot.module.auth.model.oauth;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum OAuthProvider {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플");

    private final String value;
}
