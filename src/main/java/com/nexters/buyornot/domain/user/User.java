package com.nexters.buyornot.domain.user;

import com.nexters.buyornot.domain.model.Gender;
import com.nexters.buyornot.domain.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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

    private Date birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private SocialInfo socialInfo;
}
