package com.nexters.buyornot.module.post.application;

import com.nexters.buyornot.global.common.codes.ErrorCode;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.application.ArchiveService;
import com.nexters.buyornot.module.model.EntityStatus;
import com.nexters.buyornot.module.post.api.dto.request.FromArchive;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ArchiveService archiveService;
    private static final String PROFILE = "src/main/resources/모자.png";

    @Test
    @Transactional
    void 글_작성() {
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("test", "test", PublicStatus.PUBLIC, true, urls);

        //when
        PostResponse response = postService.create(user, createPostReq);

        log.info("compare");

        //then
        assertThat(response.getId()).isEqualTo(postRepository.findByTitle(createPostReq.getTitle()).getId());
        log.info("response ->{}", response.getPollItemResponseList().get(0).getItemUrl());
    }

    @Test
    @Transactional
    void 메인화면() {
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq1 = CreatePostReq.of("test", "test", PublicStatus.PUBLIC, true, urls);
        CreatePostReq createPostReq2 = CreatePostReq.of("test", "test", PublicStatus.PRIVATE, true, urls);
        postService.create(user, createPostReq1);
        postService.create(user, createPostReq1);
        postService.create(user, createPostReq1);
        postService.create(user, createPostReq2);

        List<PostResponse> list = postService.getPage(user, 0, 20);

        for(PostResponse response : list) {
            log.info("Id: " + response.getId() + " title: " + response.getTitle() + " status: " + response.getPublicStatus() + " isPublished: " + response.isPublished());
        }

        assertThat(list.size()).isEqualTo(5);
    }

    @Test
    @Transactional
    void get_temporaries() {

        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("temporary1", "test", PublicStatus.PUBLIC, false, urls);

        //when
        PostResponse postResponse = postService.create(user, createPostReq);

        //then
        assertThat(postResponse.getTitle()).isEqualTo("temporary1");
        assertThat(postService.getTemporaries(user).size()).isEqualTo(1);
        assertThat(postResponse.getPollStatus()).isEqualTo(PollStatus.TEMPORARY);
    }

    @Test
    @Transactional
    void update_post() {

        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("임시 저장 테스트", "test", PublicStatus.PRIVATE, false, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        List<String> updateUrl = new ArrayList<>();
        updateUrl.add("https://zigzag.kr/catalog/products/113607837");
        updateUrl.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq updatePostReq = CreatePostReq.of("update1", "test", PublicStatus.PUBLIC, true, updateUrl);

        //when
        PostResponse response = postService.updatePost(user, postResponse.getId(), updatePostReq);

        //then
        assertThat(response.getTitle()).isEqualTo("update1");
        assertThat(response.isPublished()).isEqualTo(true);
    }

    @Test
    @Transactional
    public void delete() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("임시 저장 테스트", "test", PublicStatus.PUBLIC, false, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        //when
        Post post = postRepository.findById(postResponse.getId()).get();
        postService.deletePost(user, postResponse.getId());

        //then
        assertThat(post.getEntityStatus()).isEqualTo(EntityStatus.DELETED);
    }

    @Test
    @Transactional
    public void 아카이브에서_글작성() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        ArchiveResponse archiveResponse1 = archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/2028329");
        ArchiveResponse archiveResponse2 = archiveService.saveFromWeb(user, "https://zigzag.kr/catalog/products/113607837");
        FromArchive dto = new FromArchive("아카이브 글 작성 테스트", "테스트", PublicStatus.PUBLIC, true);

        //when
        log.info("---글 작성---");
        PostResponse response = postService.createFromArchive(user, archiveResponse1.getItemId(), archiveResponse2.getItemId(), dto);

        //then
        assertThat(response.getTitle()).isEqualTo("아카이브 글 작성 테스트");
    }

    @Test
    @Transactional
    public void 투표_종료() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        CreatePostReq createPostReq = CreatePostReq.of("투표 종료 테스트", "테스트", PublicStatus.PUBLIC, true, urls);
        PostResponse postResponse = postService.create(user, createPostReq);
        assertThat(postResponse.getPollStatus()).isEqualTo(PollStatus.ONGOING);

        //when
        String result = postService.endPoll(user, postResponse.getId());

        //when
        assertThat(result).isEqualTo(PollStatus.CLOSED.name());
    }

    @Test
    @Transactional
    public void 내_글_목록() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        CreatePostReq createPostReq1 = CreatePostReq.of("내 글 목록 테스트1", "테스트", PublicStatus.PUBLIC, true, urls);
        CreatePostReq createPostReq2 = CreatePostReq.of("내 글 목록 테스트2", "테스트", PublicStatus.PUBLIC, true, urls);
        CreatePostReq createPostReq3 = CreatePostReq.of("내 글 목록 테스트3", "테스트", PublicStatus.PUBLIC, true, urls);

        PostResponse postResponse1 = postService.create(user, createPostReq1);
        PostResponse postResponse2 = postService.create(user, createPostReq2);
        PostResponse postResponse3 = postService.create(user, createPostReq3);
        assertThat(postResponse1.getPollStatus()).isEqualTo(PollStatus.ONGOING);
        assertThat(postResponse2.getPollStatus()).isEqualTo(PollStatus.ONGOING);
        assertThat(postResponse3.getPollStatus()).isEqualTo(PollStatus.ONGOING);

        //when
        String afterEndPoll1 = postService.endPoll(user, postResponse1.getId());
        String afterEndPoll2 = postService.endPoll(user, postResponse2.getId());
        assertThat(afterEndPoll1).isEqualTo(PollStatus.CLOSED.name());
        assertThat(afterEndPoll2).isEqualTo(PollStatus.CLOSED.name());
        List<PostResponse> ongoing = postService.getOngoing(user, 0, 5);
        List<PostResponse> closed = postService.getClosed(user, 0, 5);

        //then
        assertThat(ongoing.size()).isEqualTo(1);
        assertThat(closed.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void 임시저장_개수_초과() {
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");


        for (int i = 0; i < 10; i++) {
            CreatePostReq createPostReq = CreatePostReq.of("임시 저장 테스트" + i, "테스트", PublicStatus.PRIVATE, false, urls);

            if (i == 5) {
                log.info("count: " + i);

                assertThatThrownBy(() -> postService.create(user, createPostReq))
                        .isEqualTo(new BusinessExceptionHandler(ErrorCode.STORAGE_COUNT_EXCEEDED));
            }

            postService.create(user, createPostReq);
        }
    }

    @Test
    @Transactional
    public void 임시저장_출간() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("temp", "test", PublicStatus.PRIVATE, false, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        List<String> updateUrl = new ArrayList<>();
        updateUrl.add("https://zigzag.kr/catalog/products/113607837");
        updateUrl.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq updatePostReq = CreatePostReq.of("임시 저장 출간 테스트", "test", PublicStatus.PUBLIC, true, updateUrl);

        //when
        PostResponse response = postService.publish(user, postResponse.getId(), updatePostReq);

        //then
        assertThat(response.getTitle()).isEqualTo("임시 저장 출간 테스트");
        assertThat(response.isPublished()).isEqualTo(true);
        assertThat(postResponse.getId()).isLessThan(response.getId());
    }
}