package com.nexters.buyornot.module.user.domain;

import com.nexters.buyornot.module.auth.model.oauth.OAuthProvider;
import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Gender;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import com.nexters.buyornot.module.user.api.dto.ProfileResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String name;

    @Column(length = 100)
    private String email;

    @Unique
    private String nickname;

    private String profile;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String ageRange;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Builder
    public User(String name, String gender, String email, String nickname, String profile, OAuthProvider oAuthProvider, String ageRange, Role role) {
        this.name = name;
        this.gender = Gender.valueOf(gender);
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.ageRange = ageRange;
        this.oAuthProvider = oAuthProvider;
        this.role = role;
    }

    public JwtUser toJwtUser() {
        return JwtUser.fromUser(id, nickname, role.getValue(), profile);
    }

    public User(String name) {
        this.name = name;
    }

    public ProfileResponse getProfileResponse() {
        return new ProfileResponse(email, name, nickname, profile);
    }
}
