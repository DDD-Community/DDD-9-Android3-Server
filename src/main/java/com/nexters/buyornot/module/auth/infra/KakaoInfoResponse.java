package com.nexters.buyornot.module.auth.infra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexters.buyornot.module.auth.model.oauth.OAuthInfoResponse;
import com.nexters.buyornot.module.auth.model.oauth.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
        private String gender;
        private String age_range;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    @Override
    public String getGender() {
        if (kakaoAccount.gender == null) {
            return "etc";
        }
        return kakaoAccount.gender;
    }

    @Override
    public String getAgeRange() {
        if (kakaoAccount.age_range == null) {
            return "";
        }
        return kakaoAccount.age_range;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.getNickname();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
