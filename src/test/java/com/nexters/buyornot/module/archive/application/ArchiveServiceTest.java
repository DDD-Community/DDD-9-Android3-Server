package com.nexters.buyornot.module.archive.application;

import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.dao.ArchiveRepository;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.application.PostService;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ArchiveServiceTest {

    @Autowired
    ArchiveService archiveService;
    @Autowired
    ArchiveRepository archiveRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    @Transactional
    void 웹에서_저장() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        String url = "https://www.musinsa.com/app/goods/2028329";

        //when
        ArchiveResponse newArchive = archiveService.saveFromWeb(user, url);
        log.info("새로운 상품 저장: " + newArchive.getUpdatedAt());

        log.info("-------중복 저장-------");

        //이미 저장된 상품일 때
        ArchiveResponse oldOne = archiveService.saveFromWeb(user, url);
        log.info("상품 정보 업데이트: " + oldOne.getUpdatedAt());

        //then
        assertThat(newArchive.getUpdatedAt()).isNotEqualTo(oldOne.getUpdatedAt());
    }

    @Test
    @Transactional
    void 게시물에서_저장() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("아카이브 저장 테스트!!", "test", PublicStatus.TEMPORARY_STORAGE, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        //when
        ArchiveResponse response = archiveService.saveFromPost(user, postResponse.getPollItemResponseList().get(0).getItemId());

        //then
        assertThat(response.getItemUrl()).isEqualTo(postResponse.getPollItemResponseList().get(0).getItemUrl());
    }

    @Test
    @Transactional
    void 좋아요() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        ArchiveResponse archive = archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/2028329");

        //when
        ArchiveResponse response = archiveService.likeArchive(user, archive.getId());

        //then
        assertThat(response.isLiked()).isEqualTo(true);
    }

    @Test
    @Transactional
    void 아카이브_리스트() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");

        archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/2028329");
        archiveService.saveFromWeb(user, "https://zigzag.kr/catalog/products/113607837");
        archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        archiveService.saveFromWeb(user, "https://product.29cm.co.kr/catalog/2142915");
        archiveService.saveFromWeb(user, "https://www.wconcept.co.kr/Product/303147448");


        //when
        List<ArchiveResponse> responseList = archiveService.getAll(user, 0, 3);

        for(ArchiveResponse response : responseList) {
            log.info("response: " + response.getUpdatedAt());
        }

        //then
        assertThat(responseList.size()).isEqualTo(3);
    }
}