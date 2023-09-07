package com.nexters.buyornot.module.post.application;

import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.global.utils.RedisUtil;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.post.api.dto.response.PollResponse;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.dao.poll.ParticipantRepository;
import com.nexters.buyornot.module.post.dao.poll.UnrecommendedRepository;
import com.nexters.buyornot.module.post.domain.poll.Participant;
import com.nexters.buyornot.module.post.domain.poll.Unrecommended;
import com.nexters.buyornot.module.post.domain.post.PollItem;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.nexters.buyornot.global.common.codes.ErrorCode.NOT_FOUND_POST_EXCEPTION;
import static com.nexters.buyornot.global.common.constant.RedisKey.POLL_DEFAULT;
import static com.nexters.buyornot.global.common.constant.RedisKey.POLL_UPDATE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PollService {

    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;
    private final UnrecommendedRepository unrecommendedRepository;
    private final RedisUtil redis;
    protected static final String NON_MEMBER = "non-member";
    protected static final Long UNRECOMMENDED = 0L;
    protected static long uniqueNum = 0;
    protected static final long duration = 3600*2; //2시간

    @Transactional
    public PollResponse takePoll(Long postId, JwtUser user, Long poll) {
        String key = String.format(POLL_DEFAULT + "%s", postId);
        String userId;

        //비회원
        if(user.getRole().equals(Role.NON_MEMBER.getValue())) userId = postId + NON_MEMBER + uniqueNum++ +  LocalDateTime.now();
        else userId = user.getId().toString();

        //item id
        List<Long> pollItemList = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION))
                .getItemList();

        //캐시 없다면 가져오기
        if(!redis.exists(key)) DBtoCache(postId, pollItemList);

        //투표 안했다면
        if(!redis.alreadyPolled(key, userId)) {
            log.info(userId + " takes a poll to " + poll);
            redis.addPoll(key, userId, poll);
            redis.expire(key, duration);
        }

        //업데이트 정보 저장
        redis.sAdd(POLL_UPDATE, postId);
        redis.expire(key, duration);

        //choices
        List<Long> polls = redis.getPollsByPost(key);
        log.info("poll size: " + polls.size());

        //choice : num
        Map<Long, Integer> status = new TreeMap<>();

        for(Long id : pollItemList) {
            int count = Collections.frequency(polls, id);
            status.put(id, count);
        }
        status.put(UNRECOMMENDED, Collections.frequency(polls, UNRECOMMENDED));
        long polled = redis.getItem(key, userId);
        return new PollResponse(status.values(), polled);
    }

    public void DBtoCache(Long postId, List<Long> pollItemList) {
        String key = String.format(POLL_DEFAULT + "%s", postId);

        for(Long itemId : pollItemList) {
            List<Participant> participants = participantRepository.findByPollItemId(itemId);
            for(Participant participant : participants) {
                redis.getPoll(key, participant.getUserId(), itemId);
            }
        }

        List<Unrecommended> unrecommendedList = unrecommendedRepository.findByPostId(postId);

        for(Unrecommended unrecommended : unrecommendedList)
            redis.getPoll(key, unrecommended.getUserId(), UNRECOMMENDED);

        redis.expire(key, duration);
    }

    @Transactional
    public void storePoll(Long postId, Map<Object, Object> updates) throws Exception {
        //updates -> userId:choice

        log.info("투표 상태 DB update");
        log.info("update post id: " + postId);

        Post post = postRepository.findById(1L)
                .orElse(null);

        if(post == null) return;

        for(Object key : updates.keySet()) {
            String userId = key.toString();
            String choice = updates.get(key).toString();

            log.info("투표자: " + userId + " choice: " + choice);

            if(choice.equals(UNRECOMMENDED)) {
                Unrecommended unrecommended = Unrecommended.newPoll(post, userId);
                unrecommendedRepository.save(unrecommended);
            } else {
                PollItem pollItem = post.getPollItem(Long.parseLong(choice));

                //만약 포스트가 삭제됐다면
                if(pollItem == null) return;

                Participant participant = Participant.newPoll(pollItem, userId);
                participantRepository.save(participant);
            }
        }
    }
}
