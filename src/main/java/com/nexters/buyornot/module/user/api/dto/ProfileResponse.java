package com.nexters.buyornot.module.user.api.dto;

import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProfileResponse {
    private String email;
    private String name;
    private String nickname;
    private String profile;

    public ProfileResponse(String email, String name, String nickname, String profile) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profile = profile;
    }
}
