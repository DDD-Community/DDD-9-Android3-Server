package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.dao.querydsl.dto.Entry;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.util.StopWatch;

@Slf4j
@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void 무한스크롤_테스트() {
        //given
        Pageable pageable = PageRequest.of(0, 5);

        //when
        Slice<PostResponse> responses = postRepository.getMain(pageable);

        //then
//        assertThat(responses.getSize() == 5);
    }

    @Test
    public void 시간_비교() {
        Pageable pageable = PageRequest.of(0, 5);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        postRepository.getMain(pageable);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch.getTotalTimeSeconds());

        stopWatch.start();

        postRepository.findPageByIsPublishedAndPublicStatusOrderByIdDesc(true, PublicStatus.PUBLIC, pageable);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch.getTotalTimeSeconds());
    }

    @Test
    public void 참가자() {
        Entry entry = postRepository.getParticipants(18L);
        log.info("[getParticipants] " + entry.toString());
    }
}