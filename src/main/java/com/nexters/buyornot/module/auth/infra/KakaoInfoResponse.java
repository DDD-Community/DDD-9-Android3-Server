package com.nexters.buyornot.module.auth.infra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexters.buyornot.module.auth.model.oauth.OAuthInfoResponse;
import com.nexters.buyornot.module.auth.model.oauth.OAuthProvider;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        private String birthday;
        private String age_range;

    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    @Override
    public String getGender() { return kakaoAccount.gender; }

    @Override
    public String getAgeRange() { return kakaoAccount.age_range; }

    @Override
    public Date getBirthday() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM월 dd일");
        Date date;
        try {
            date = formatter.parse(kakaoAccount.getBirthday());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return date;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        String uniqueNickname = kakaoAccount.profile.nickname;
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmm");
        Calendar dateTime = Calendar.getInstance();
        uniqueNickname += format.format(dateTime.getTime());
        uniqueNickname += "_" + RandomStringUtils.randomAlphabetic(5);
        return uniqueNickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
