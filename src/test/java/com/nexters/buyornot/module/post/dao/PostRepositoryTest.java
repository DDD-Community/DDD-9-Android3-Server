package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.user.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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
    public void findByUserIdAndIsPublishedTest() {
        UUID id = UUID.fromString("449e9ce4-6c4f-4c61-a9aa-2808148323e6");
        boolean isPublished = true;
        List<Post> result = postRepository.findByUserIdAndIsPublished(id, isPublished);
        result.forEach(System.out::println);
    }
}