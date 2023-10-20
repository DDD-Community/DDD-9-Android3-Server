package com.nexters.buyornot.global.schedule;

import com.nexters.buyornot.global.utils.RedisUtil;
import com.nexters.buyornot.module.model.EntityStatus;
import com.nexters.buyornot.module.post.application.PollService;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nexters.buyornot.global.common.constant.RedisKey.POLL_DEFAULT;
import static com.nexters.buyornot.global.common.constant.RedisKey.POLL_UPDATE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final RedisUtil redis;
    private final PollService pollService;
    private final UserRepository userRepository;
    private static final long duration = 3600*2;   // 2시간

    @Transactional
    @Scheduled(fixedDelay = 3000000L) //50분마다
//    @Scheduled(fixedDelay = 10000) //for test
    public void cacheToDB() throws Exception {

        log.info("start poll scheduling: " + LocalDateTime.now());

        //업데이트 사항이 있는 글 리스트
        Set<Integer> updatedIds = redis.sMembers(POLL_UPDATE)
                .stream()
                .map(i -> (Integer) i).collect(Collectors.toSet());

        redis.del(POLL_UPDATE);

        for(Integer postId : updatedIds) {
            Long id = Long.parseLong(postId.toString());
            String key = String.format(POLL_DEFAULT + "%s", id);

            //유저 id, 투표 항목
            Map<Object, Object> updates = redis.getPollStatus(key);

            try {
                pollService.storePoll(id, updates);
            } catch (Exception e) {
                redis.sAdd(POLL_UPDATE, id);
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 30 10 ? * 6L")
    public void delete() throws Exception {
        log.info("start delete scheduling: " + LocalDateTime.now());
        List<User> userList = userRepository.findByEntityStatus(EntityStatus.DELETED);
        List<UUID> deleteList = userList.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        userRepository.deleteAllById(deleteList);
    }
}
