package com.nexters.buyornot.module.post.application;

import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.response.PollResponse;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.dao.poll.UnrecommendedRepository;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class PollServiceTest {

    @Autowired
    private PollService pollService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UnrecommendedRepository unrecommendedRepository;
    @Autowired
    private PostService postService;
    static final Long UNRECOMMENDED = 0L;
    private static final String PROFILE = "src/main/resources/모자.png";
    @BeforeEach
    void set() {
        JwtUser user = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);

        List<String> item = new ArrayList<>() {
            {
                add("https://zigzag.kr/catalog/products/113607837");
                add("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
            }
        };

        CreatePostReq dto = new CreatePostReq("poll test", "poll test", PublicStatus.PUBLIC, item, true);

        postService.create(user, dto);
    }

    @Test
    @Transactional
    void 비회원_투표() {
        //given
        JwtUser nonMember = new JwtUser();
        Post post = postRepository.findByTitle("poll test");

        List<Long> itemList = post.getItemList();

        //when
        pollService.takePoll(post.getId(), nonMember, itemList.get(0));
        pollService.takePoll(post.getId(), nonMember, itemList.get(1));
        PollResponse response = pollService.takePoll(post.getId(), nonMember, UNRECOMMENDED);

        //then
        assertThat(response.getUnrecommended()).isEqualTo(1);
    }

    @Test
    @Transactional
    void 회원_중복_투표() {
        JwtUser member = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina", PROFILE);

        Post post = postRepository.findByTitle("poll test");

        //when
        pollService.takePoll(post.getId(), member, 0L);
        pollService.takePoll(post.getId(), member, 0L);
        PollResponse response = pollService.takePoll(post.getId(), member, 0L);

        //then
        assertThat(response.getUnrecommended()).isEqualTo(1);
    }

//    @Test
//    @Transactional
//    void 캐시_쓰기() {
//        //given
//        Post post = postRepository.findByTitle("poll test");
//        log.info("test post id: " + post.getId());
//        JwtUser member = JwtUser.fromUser(UUID.randomUUID(), "mina", "mina");
//        pollService.takePoll(post.getId(), member, 0L);
//
//        //when
//        List<Unrecommended> unrecommendedList = unrecommendedRepository.findByPostId(post.getId());
//        log.info("before write: " + unrecommendedList.size());
//
//        Awaitility.await()
//                .pollDelay(Duration.ofSeconds(50))
//                .pollInterval(Duration.ofSeconds(5))
//                .atMost(2, TimeUnit.MINUTES)
//                .untilAsserted(() -> {
//                    List<Unrecommended> afterWrite = unrecommendedRepository.findByPostId(post.getId());
//                    assertThat(afterWrite.size()).isEqualTo(1);
//                });
//    }
}