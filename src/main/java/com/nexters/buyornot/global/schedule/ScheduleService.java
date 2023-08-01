package com.nexters.buyornot.global.schedule;

import com.nexters.buyornot.global.utils.RedisUtil;
import com.nexters.buyornot.module.post.application.PollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final RedisUtil redis;
    private final PollService pollService;
    private static final long duration = 3600*2;   // 2시간
    protected static final String keyPrefix = "bon:poll:";
    protected static final String keyUpdate = "key:update";

    @Transactional
    @Scheduled(fixedDelay = 3000000L) //50분마다
//    @Scheduled(fixedDelay = 10000) //for test
    public void cacheToDB() throws Exception {

        log.info("start poll scheduling: " + LocalDateTime.now());

        //업데이트 사항이 있는 글 리스트
        Set<Integer> updatedIds = redis.sMembers(keyUpdate)
                .stream()
                .map(i -> (Integer) i).collect(Collectors.toSet());

        redis.del(keyUpdate);

        for(Integer postId : updatedIds) {
            Long id = Long.parseLong(postId.toString());
            String key = String.format(keyPrefix + "%s", id);

            //유저 id, 투표 항목
            Map<Object, Object> updates = redis.getPollStatus(key);

            try {
                pollService.storePoll(id, updates);
            } catch (Exception e) {
                redis.sAdd(keyUpdate, id);
            }
        }
    }

}
