package com.nexters.buyornot.module.user.api.dto;

import com.nexters.buyornot.module.model.Role;
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
    private String profile;
    private String role = Role.NON_MEMBER.getValue();

    public static JwtUser newJwtUser(Claims claims) {
        return builder()
                .id(UUID.fromString(claims.getSubject()))
                .nickname(String.valueOf(claims.get("nickname")))
                .profile(String.valueOf(claims.get("profile")))
                .role(String.valueOf(claims.get("role")).isEmpty() ? "NON_MEMBER" : String.valueOf(claims.get("role")))
                .build();
    }

    public static JwtUser fromUser(UUID id, String nickname, String role, String profile) {
        return JwtUser.builder()
                .id(id)
                .nickname(nickname)
                .profile(profile)
                .role(role.isEmpty() ? "NON_MEMBER" : role)
                .build();
    }
}
