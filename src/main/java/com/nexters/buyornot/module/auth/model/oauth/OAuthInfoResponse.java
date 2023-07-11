package com.nexters.buyornot.module.auth.model.oauth;

public interface OAuthInfoResponse {
    String getGender();
    String getAgeRange();
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
