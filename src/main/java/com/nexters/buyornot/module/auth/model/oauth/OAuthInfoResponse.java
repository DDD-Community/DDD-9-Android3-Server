package com.nexters.buyornot.module.auth.model.oauth;

import java.util.Date;

public interface OAuthInfoResponse {
    String getGender();
    String getAgeRange();
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
