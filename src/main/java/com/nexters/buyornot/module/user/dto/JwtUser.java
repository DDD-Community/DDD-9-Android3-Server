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
    private String name;
    private String nickname;
    private String email;
    private String role;

    public static JwtUser newJwtUser(Claims claims) {
        return builder()
                .id(UUID.fromString(claims.getSubject()))
                .name(String.valueOf(claims.get("name")))
                .nickname(String.valueOf(claims.get("nickname")))
                .email(String.valueOf(claims.get("email")))
                .role(String.valueOf(claims.get("role")))
                .build();
    }

    public static JwtUser fromUser(UUID id, String name, String nickname, String email, String role) {
        return JwtUser.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .email(email)
                .role(role)
                .build();
    }


}
