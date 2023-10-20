package com.nexters.buyornot.module.user.application;

import com.nexters.buyornot.global.jwt.JwtTokenProvider;
import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.auth.model.oauth.OAuthProvider;
import com.nexters.buyornot.module.model.Gender;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.Nickname;
import com.nexters.buyornot.module.user.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Log4j2
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private static final String PROFILE = "src/main/resources/모자.png";
    private static final String path = "resources/profile";
    private static final String EXTENSION = ".png";
    private static final String PREFIX = "익명의";

    @Test
    @Transactional
    void 프로필_랜덤() {
        double min = 0;
        double max = 19;
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int random = (int) ((Math.random() * (max - min)) + min);
            int num = rand.nextInt(10000);

            Nickname[] arr = Nickname.values();
            Nickname profile = arr[random];

            String pic = path + "/" + profile.getValue() + EXTENSION;
            String nickname = PREFIX + profile.getValue() + num;
//            log.info(pic);
            log.info("nickname: " + nickname);
        }
    }

    @Test
    @DisplayName("테스트를 위한 더미 유저 생성")
    public void createTestDummyUserTest() {
        // given
        int id = (int) (Math.random() * 1000) + 1;
        String userId = "buyornot" + id;
        // when

        // then

        // 유저 생성
        User user = User.builder()
                .name(userId)
                .gender(Gender.male.name())
                .email(userId + "@gmail.com")
                .profile(userId + "profile")
                .oAuthProvider(OAuthProvider.KAKAO)
                .role(Role.ADMIN)
                .build();
        User savedUser = userRepository.save(user);

        JwtUser jwtUser = savedUser.toJwtUser();
        log.info(String.valueOf(jwtUser.getId()));

        // 유저의 JWT Token 생성
        AuthTokens authUser = jwtTokenProvider.generate(jwtUser);
        log.info("USER's JWT TOKEN");
        log.info("Access Token: " + authUser.getAccessToken());
        log.info("Refresh Token: " + authUser.getRefreshToken());
        log.info("Grant Type: " + authUser.getGrantType());
    }

    @Test
    @DisplayName("Get User JWT Token Test")
    public void getUserJWTToken() {
        // given
        UUID id = UUID.fromString("c3aa8b91-71aa-4091-bb33-54797e885f38");
        // when
        User user = userRepository.findById(id).get();
        Assertions.assertNotNull(user);
        // then
        JwtUser jwtUser = user.toJwtUser();
        log.info("JWT User's INFO");
        log.info("id: " + jwtUser.getId());
        log.info("nickname: " + jwtUser.getNickname());
        log.info("profile: " + jwtUser.getProfile());
        log.info("role: " + jwtUser.getRole());

        AuthTokens authUser = jwtTokenProvider.generate(jwtUser);
        log.info("USER's JWT TOKEN");
        log.info("Access Token: " + authUser.getAccessToken());
        log.info("Refresh Token: " + authUser.getRefreshToken());
        log.info("Grant Type: " + authUser.getGrantType());
    }
}