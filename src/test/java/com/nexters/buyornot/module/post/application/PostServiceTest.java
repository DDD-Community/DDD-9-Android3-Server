package com.nexters.buyornot.module.post.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.nexters.buyornot.module.post.dao.PostRepository;
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
    void create_post() {

        log.info("===========================================");

        log.info("유저 저장");

        //given
        User user = new User("mina");

        JwtUser jwtUser = user.toJwtUser();

        log.info("create post dto");

        List<String> urls = new ArrayList<>();
        urls.add("https://zigzag.kr/catalog/products/113607837");
        urls.add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");

        CreatePostReq createPostReq = CreatePostReq.of("test", "test", PublicStatus.PUBLIC, urls);

        log.info("service.create");

        //when
        PostResponse response = postService.create(jwtUser, createPostReq);

        log.info("compare");

        //then
        assertThat(response.getId()).isEqualTo(postRepository.findByTitle(createPostReq.getTitle()).getId());
        log.info("response ->{}", response.getPollItemResponseList().get(0).getItemUrl());
        log.info("===========================================");
    }
}