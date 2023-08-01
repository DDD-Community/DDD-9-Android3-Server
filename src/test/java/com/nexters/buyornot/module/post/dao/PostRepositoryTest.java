package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void fetch_join_test() {
        List<Post> posts = postRepository.findAllFetchJoin();

        log.info("=================N+1==========================");
        List<PostResponse> responseList = posts
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());
    }

    @Test
    @Transactional
    public void paging() {

        Page<Post> all = postRepository.findAll(PageRequest.of(0, 5));

        log.info("=================N+1==========================");

        List<PostResponse> responseList = all
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());

        int idx = 0;
        for(PostResponse response : responseList) {
            log.info(idx + " " + response.getTitle());
            idx++;
        }

        log.info("===========================================");
    }

    @Test
    @Transactional
    public void pagingByUser() {

        //given
        User user = userRepository.findByNickname("홍민아2307212021_FzcuG");

        log.info("user id: " + user.getId());

        //when
        List<PostResponse> responseList = postRepository.findPageByUser(user.getId(), PublicStatus.PUBLIC, PageRequest.of(0, 6))
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());

        //then
//        assertThat(responseList.size()).isEqualTo(6);
    }

}