package com.nexters.buyornot.module.user.domain;

import com.nexters.buyornot.module.auth.model.oauth.OAuthProvider;
import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Gender;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.user.dto.JwtUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    private Role role;

    private String name;

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Column(unique = true, length = 100)
    private String email;

    @Unique
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String ageRange;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Builder
    public User(String gender, String email, String nickname, OAuthProvider oAuthProvider, String ageRange, Role role) {
        this.name = nickname;
        this.gender = Gender.valueOf(gender);
        this.email = email;
        this.nickname = nickname;
        this.ageRange = ageRange;
        this.oAuthProvider = oAuthProvider;
        this.role = role;
    }

    public JwtUser toJwtUser() {
        return JwtUser.fromUser(id, name, nickname, email, role.getValue());
    }

    public User(String name) {
        this.name = name;
    }
}
