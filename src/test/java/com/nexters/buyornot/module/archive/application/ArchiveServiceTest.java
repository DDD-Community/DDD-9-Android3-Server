package com.nexters.buyornot.module.archive.application;

import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.dao.ArchiveRepository;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArchiveServiceTest {

    @Autowired
    ArchiveService archiveService;
    @Autowired
    ArchiveRepository archiveRepository;

    @Test
    @Transactional
    void 웹에서_저장() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        String url = "https://www.musinsa.com/app/goods/2028329";

        //when
        ArchiveResponse newArchive = archiveService.save(user, url);
        log.info("새로운 상품 저장: " + newArchive.getUpdatedAt());

        log.info("-------중복 저장-------");

        //이미 저장된 상품일 때
        ArchiveResponse oldOne = archiveService.save(user, url);
        log.info("상품 정보 업데이트: " + oldOne.getUpdatedAt());

        //then
        assertThat(newArchive.getUpdatedAt()).isNotEqualTo(oldOne.getUpdatedAt());
    }
}