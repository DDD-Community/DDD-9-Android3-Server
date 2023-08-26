package com.nexters.buyornot.module.user.application;

import com.nexters.buyornot.module.user.domain.Nickname;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class UserServiceTest {

    private static final String path = "resources/profile";
    private static final String EXTENSION = ".png";
    private static final String PREFIX = "익명의";

    @Test
    @Transactional
    void 프로필_랜덤() {
        double min = 0;
        double max = 19;
        Random rand = new Random();

        for(int i = 0; i < 10; i++) {
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
}