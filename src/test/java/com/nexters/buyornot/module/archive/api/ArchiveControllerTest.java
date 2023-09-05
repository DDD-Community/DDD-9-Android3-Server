package com.nexters.buyornot.module.archive.api;

import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.application.ArchiveService;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatStream;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArchiveControllerTest {

    @Autowired
    ArchiveService archiveService;
    @Autowired
    ArchiveController archiveController;

    @Test
    @Transactional
    void likeArchive() {
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina");
        ArchiveResponse creation = archiveController.fromWeb(user, "https://www.musinsa.com/app/goods/2028329").getBody().getResult();

        assertThat(creation.isLiked()).isEqualTo(false);

        ArchiveResponse response1 = archiveController.likeArchive(user, creation.getId()).getBody().getResult();
        log.info("like: " + response1.isLiked());
        assertThat(response1.isLiked()).isEqualTo(true);
        ArchiveResponse response2 = archiveController.likeArchive(user, creation.getId()).getBody().getResult();
        log.info("like: " + response2.isLiked());
        assertThat(response2.isLiked()).isEqualTo(false);
    }
}