package com.nexters.buyornot.module.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserSocialType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플");

    private final String value;
}
