package com.nexters.buyornot.module.user.dto;

import io.jsonwebtoken.Claims;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUser implements Serializable {

    private UUID id;
    private String nickname;
    private String role = "NON_MEMBER";

    public static JwtUser newJwtUser(Claims claims) {
        return builder()
                .id(UUID.fromString(claims.getSubject()))
                .nickname(String.valueOf(claims.get("nickname")))
                .role(String.valueOf(claims.get("role")))
                .build();
    }

    public static JwtUser fromUser(UUID id, String nickname, String role) {
        return JwtUser.builder()
                .id(id)
                .nickname(nickname)
                .role(role)
                .build();
    }


}
