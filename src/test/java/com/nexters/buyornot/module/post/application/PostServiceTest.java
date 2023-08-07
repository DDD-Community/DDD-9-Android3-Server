package com.nexters.buyornot.module.post.application;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ArchiveService archiveService;

    @Test
    @Transactional
    void 글_작성() {

        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("test", "test", PublicStatus.PUBLIC, urls);

        //when
        PostResponse response = postService.create(user, createPostReq);

        log.info("compare");

        //then
        assertThat(response.getId()).isEqualTo(postRepository.findByTitle(createPostReq.getTitle()).getId());
        log.info("response ->{}", response.getPollItemResponseList().get(0).getItemUrl());
    }

    @Test
    @Transactional
    void get_temporaries() {

        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("임시 저장 테스트", "test", PublicStatus.TEMPORARY_STORAGE, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        //when
        List<PostResponse> temporaries = postService.getTemporaries(user);

        //then
        log.info(temporaries.get(0).getTitle());
        assertThat(temporaries.get(0).getTitle()).isEqualTo("temporary1");
    }

    @Test
    @Transactional
    void update_post() {

        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("임시 저장 테스트", "test", PublicStatus.TEMPORARY_STORAGE, urls);
        PostResponse postResponse = postService.create(user, createPostReq);

        List<String> updateUrl = new ArrayList<>();
        updateUrl.add("https://zigzag.kr/catalog/products/113607837");
        updateUrl.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq updatePostReq = CreatePostReq.of("update1", "test", PublicStatus.PUBLIC, updateUrl);

        //when
        postService.updatePost(user, postResponse.getId(), updatePostReq);

        //then
        assertThat(postRepository.findById(postResponse.getId()).get().newPostResponse().getTitle()).isEqualTo("update1");
    }

    @Test
    @Transactional
    public void delete() {
        //given
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("임시 저장 테스트", "test", PublicStatus.TEMPORARY_STORAGE, urls);
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
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        ArchiveResponse archiveResponse1 = archiveService.saveFromWeb(user, "https://www.musinsa.com/app/goods/2028329");
        ArchiveResponse archiveResponse2 = archiveService.saveFromWeb(user, "https://zigzag.kr/catalog/products/113607837");
        FromArchive dto = new FromArchive("아카이브 글 작성 테스트", "테스트", PublicStatus.PUBLIC);

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
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", "mina@mina", "ROLE_USER");
        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        CreatePostReq createPostReq = CreatePostReq.of("투표 종료 테스트", "테스트", PublicStatus.PUBLIC, urls);
        PostResponse postResponse = postService.create(user, createPostReq);
        assertThat(postResponse.getPollStatus()).isEqualTo(PollStatus.ONGOING.name());

        //when
        PostResponse result = postService.endPoll(user, postResponse.getId());

        //when
        assertThat(result.getPollStatus()).isEqualTo(PollStatus.CLOSED.name());
    }
}