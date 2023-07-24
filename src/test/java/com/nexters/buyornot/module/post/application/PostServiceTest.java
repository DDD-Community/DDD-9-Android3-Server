package com.nexters.buyornot.module.post.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.nexters.buyornot.module.model.EntityStatus;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.domain.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.dto.response.PostResponse;
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

@SpringBootTest
@Slf4j
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    Long create_post(JwtUser jwtUser) {

        log.info("===========================================");

        log.info("유저 저장");

        log.info("create post dto");

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("temporary1", "test", PublicStatus.TEMPORARY_STORAGE, urls);

        log.info("service.create");

        //when
        PostResponse response = postService.create(jwtUser, createPostReq);

        log.info("compare");

        //then
        assertThat(response.getId()).isEqualTo(postRepository.findByTitle(createPostReq.getTitle()).getId());
        log.info("response ->{}", response.getPollItemResponseList().get(0).getItemUrl());
        log.info("===========================================");
        return response.getId();
    }
    @Test
    @Transactional
    void get_temporaries() {

        //given
        User user = new User("mina");
        User savedUser = userRepository.save(user);
        JwtUser jwtUser = savedUser.toJwtUser();

        //when
        create_post(jwtUser);

        List<PostResponse> temporaries = postService.getTemporaries(jwtUser);

        //then
        log.info(temporaries.get(0).getTitle());
        assertThat(temporaries.get(0).getTitle()).isEqualTo("temporary1");
    }

    @Test
    @Transactional
    void update_post() {

        //given
        User user = new User("mina");
        User savedUser = userRepository.save(user);
        JwtUser jwtUser = savedUser.toJwtUser();
        Long postId = create_post(jwtUser);

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("update1", "test", PublicStatus.PUBLIC, urls);

        //when
        postService.updatePost(jwtUser, postId, createPostReq);

        //then
        assertThat(postRepository.findById(postId).get().newPostResponse().getTitle()).isEqualTo("update1");
    }

    @Test
    @Transactional
    public void delete() {
        //given
        User user = new User("mina");
        User savedUser = userRepository.save(user);
        JwtUser jwtUser = savedUser.toJwtUser();
        Long postId = create_post(jwtUser);


        //when
        Post post = postRepository.findById(postId).get();
        postService.deletePost(jwtUser, postId);

        //then
        assertThat(post.getEntityStatus()).isEqualTo(EntityStatus.DELETED);
    }

}