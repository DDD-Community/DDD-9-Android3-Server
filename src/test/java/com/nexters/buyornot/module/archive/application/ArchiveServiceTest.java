package com.nexters.buyornot.module.archive.application;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.module.archive.api.dto.request.DeleteArchiveReq;
import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.dao.ArchiveRepository;
import com.nexters.buyornot.module.archive.domain.Archive;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.application.PostService;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    private static final String PROFILE = "src/main/resources/모자.png";

    @Test
    @Transactional
    void 웹에서_저장() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
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
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("아카이브 저장 테스트!!", "test", PublicStatus.PUBLIC, true, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        //when
        ArchiveResponse response = archiveService.saveFromPost(user, postResponse.getPollItemResponseList().get(0).getItemId());

        //then
        assertThat(response.getItemUrl()).isEqualTo(postResponse.getPollItemResponseList().get(0).getItemUrl());
    }
    @Test
    @DisplayName("Ably & Wconcept 게시물 저장 테스트")
    @Transactional
    void 게시물에서_저장_W_A() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);

        List<String> urls = new ArrayList<>();
        urls.add("https://m.a-bly.com/goods/11145152?param1=value1&param2=value2");
        urls.add("https://www.wconcept.co.kr/Product/303804583");

        CreatePostReq createPostReq = CreatePostReq.of("아카이브 저장 테스트!!", "test", PublicStatus.PUBLIC, true, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        //when
        ArchiveResponse response = archiveService.saveFromPost(user, postResponse.getPollItemResponseList().get(0).getItemId());

        //then
        assertThat(response.getItemUrl()).isEqualTo(postResponse.getPollItemResponseList().get(0).getItemUrl());
    }

    @Test
    void 게시물_저장_부하테스트() {
        for (int i = 0; i < 100; i++) {
            게시물에서_저장();
        }
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void 좋아요() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        ArchiveResponse archive = archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/2028329");

        //when
        ArchiveResponse response = archiveService.likeArchive(user, archive.getId());

        //then
        assertThat(response.isLiked()).isEqualTo(true);
        Archive updatedArchive = archiveRepository.findById(response.getId()).get();
        log.info("업데이트 후: " + updatedArchive.newResponse().isLiked());
        assertThat(updatedArchive.newResponse().isLiked()).isEqualTo(true);
    }

    @Test
    @Transactional
    void 아카이브_리스트() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);

        ArchiveResponse archiveResponse1 = archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/2028329");
        ArchiveResponse archiveResponse2 = archiveService.saveFromWeb(user, "https://zigzag.kr/catalog/products/113607837");
        ArchiveResponse archiveResponse3 = archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        ArchiveResponse archiveResponse4 = archiveService.saveFromWeb(user, "https://product.29cm.co.kr/catalog/2142915");
        ArchiveResponse archiveResponse5 = archiveService.saveFromWeb(user, "https://www.wconcept.co.kr/Product/303147448");

        //when
        archiveService.likeArchive(user, archiveResponse1.getId());
        archiveService.likeArchive(user, archiveResponse2.getId());
        archiveService.likeArchive(user, archiveResponse3.getId());
        archiveService.likeArchive(user, archiveResponse4.getId());
        archiveService.likeArchive(user, archiveResponse5.getId());
        List<ArchiveResponse> beforeDelete = archiveService.getAll(user, 0, 5);
        List<ArchiveResponse> likeList = archiveService.getLikes(user, 0, 5);

        assertThat(beforeDelete.size()).isEqualTo(5);

        List<Long> deleteList = new ArrayList<>();
        deleteList.add(archiveResponse1.getId());
        deleteList.add(archiveResponse2.getId());
        archiveService.delete(user, new DeleteArchiveReq(deleteList));

        //then
        List<ArchiveResponse> afterDelete = archiveService.getAll(user, 0, 3);
        assertThat(afterDelete.size()).isEqualTo(3);
        assertThat(likeList.size()).isEqualTo(5);
    }

    @Test
    void 아카이브_삭제() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        ArchiveResponse archiveResponse1 = archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/2028329");
        ArchiveResponse archiveResponse2 = archiveService.saveFromWeb(user, "https://zigzag.kr/catalog/products/113607837");
        List<Long> list = new ArrayList<>();
        list.add(archiveResponse1.getId());
        list.add(archiveResponse2.getId());
        DeleteArchiveReq deleteArchiveReq = new DeleteArchiveReq(list);

        //when
        String result = archiveService.delete(user, deleteArchiveReq);

        //then
        assertThat(result).isEqualTo(SuccessCode.DELETE_SUCCESS.getMessage());
    }
}