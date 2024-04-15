package com.nexters.buyornot.module.post.application;

import static com.nexters.buyornot.global.common.codes.ErrorCode.NOT_FOUND_ITEM_EXCEPTION;
import static com.nexters.buyornot.global.common.codes.ErrorCode.NOT_FOUND_POST_EXCEPTION;
import static com.nexters.buyornot.global.common.codes.ErrorCode.STORAGE_COUNT_EXCEEDED;
import static com.nexters.buyornot.global.common.codes.ErrorCode.UNAUTHORIZED_USER_EXCEPTION;
import static com.nexters.buyornot.global.common.constant.RedisKey.POLL_DEFAULT;
import static com.nexters.buyornot.module.post.application.PollService.NON_MEMBER;
import static com.nexters.buyornot.module.post.application.PollService.UNRECOMMENDED;
import static com.nexters.buyornot.module.post.application.PollService.duration;

import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.global.utils.RedisUtil;
import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.request.FromArchive;
import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.api.dto.response.PollResponse;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.dao.querydsl.dto.Entry;
import com.nexters.buyornot.module.post.dao.querydsl.dto.MyPostCondition;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.poll.Participant;
import com.nexters.buyornot.module.post.domain.poll.Unrecommended;
import com.nexters.buyornot.module.post.domain.post.PollItem;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final RedisUtil redis;

    @Transactional
    public PostResponse create(JwtUser jwtUser, CreatePostReq dto) {
        if (jwtUser.getRole().equals(Role.NON_MEMBER.getValue())) {
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);
        }
        if (!dto.isPublished()) {
            List<PostResponse> temporaries = postRepository.findTemporaries(jwtUser.getId());
            if (temporaries.size() >= 5) {
                throw new BusinessExceptionHandler(STORAGE_COUNT_EXCEEDED);
            }
        }

        List<PollItem> pollItems = addPollItem(dto.getItemUrls());
        Post post = Post.newPost(jwtUser, dto, pollItems);
        postRepository.save(post);
        return post.newPostResponse();
    }

    @Transactional
    public PostResponse createFromArchive(JwtUser user, Long itemId1, Long itemId2, FromArchive dto) {
        Item item1 = itemRepository.findById(itemId1)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ITEM_EXCEPTION));
        Item item2 = itemRepository.findById(itemId2)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ITEM_EXCEPTION));

        List<PollItem> pollItems = List.of(
                PollItem.newPollItem(item1.info()),
                PollItem.newPollItem(item2.info())
        );

        Post post = Post.newPost(user, dto, pollItems);
        return postRepository.save(post).newPostResponse();
    }

    private List<PollItem> addPollItem(List<String> itemUrls) {
        List<PollItem> pollItems = new ArrayList<>();

        for (String url : itemUrls) {
            Item item = itemRepository.findByItemUrl(url)
                    .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ITEM_EXCEPTION));

            pollItems.add(PollItem.newPollItem(item.info()));
        }
        return pollItems;
    }

    @Transactional
    public PostResponse publish(JwtUser user, Long postId, CreatePostReq dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));

        if (!post.checkValidity(user.getId())) {
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);
        }

        PostResponse response = create(user, dto);
        deletePost(user, postId);

        return response;
    }

    @Transactional
    public PostResponse updatePost(JwtUser user, Long postId, CreatePostReq dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));
        if (!post.checkValidity(user.getId())) {
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);
        }
        List<PollItem> pollItems = addPollItem(dto.getItemUrls());

        post.update(dto, pollItems);
        postRepository.save(post);

        return post.newPostResponse();
    }

    @Transactional
    public Long deletePost(JwtUser user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));

        if (!post.checkValidity(user.getId())) {
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);
        }

        post.delete();
        postRepository.save(post);

        return post.getId();
    }

    //todo: 투표 아이템 왜 하나만 들어가는지 확인
    public PostResponse getPost(JwtUser user, Long postId) {
        String userId = NON_MEMBER + LocalDateTime.now();

        if (!user.getRole().equals(Role.NON_MEMBER.getValue())) {
            userId = user.getId().toString();
        }

        String key = String.format(POLL_DEFAULT + "%s", postId);

        PostResponse response = postRepository.getPost(postId);

        response = addPollStatus(userId, key, postId, response);
        return response;
    }

    //전체 공개 포스트만
    public Slice<PostResponse> getMain(JwtUser user, final int page, final int count) {
        String userId = NON_MEMBER + LocalDateTime.now();

        if (!user.getRole().equals(Role.NON_MEMBER.getValue())) {
            userId = user.getId().toString();
        }

        Slice<PostResponse> main = postRepository.getMain(PageRequest.of(page, count));

        for (PostResponse post : main) {
            String key = String.format(POLL_DEFAULT + "%s", post.getId());
            addPollStatus(userId, key, post.getId(), post);
        }
        return main;
    }

    @Transactional
    public String endPoll(JwtUser user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));
        if (!post.checkValidity(user.getId())) {
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);
        }

        post.endPoll();
        postRepository.save(post);
        return PollStatus.CLOSED.name();
    }

    public Slice<PostResponse> getMyPosts(JwtUser user, boolean isOngoing, final int page, final int count) {
        PollStatus pollStatus = isOngoing ? PollStatus.ONGOING : PollStatus.CLOSED;

        Slice<PostResponse> ongoings = postRepository.getMine(
                new MyPostCondition(user.getId(), pollStatus, true, PageRequest.of(page, count)));

        for (PostResponse post : ongoings) {
            String key = String.format(POLL_DEFAULT + "%s", post.getId());
            addPollStatus(user.getId().toString(), key, post.getId(), post);
        }
        return ongoings;
    }

    public List<PostResponse> getTemporaries(JwtUser user) {
        List<PostResponse> responseList = postRepository.findTemporaries(user.getId());
        return responseList;
    }

    private PostResponse addPollStatus(String userId, String key, Long postId, PostResponse response) {
        if (!redis.exists(key)) {
            List<Long> pollItemIds = new ArrayList<>();
            for (PollItemResponse itemRes : response.getPollItemResponses()) {
                pollItemIds.add(itemRes.getItemId());
            }
            DBtoCache(postId);
        }

        List<Long> polls = redis.getPollsByPost(key);

        if (redis.alreadyPolled(key, userId) || response.getUserId().equals(userId) && response.isPublished()) {
            Map<Long, Integer> status = new HashMap<>();

            for (PollItemResponse item : response.getPollItemResponses()) {
                int count = Collections.frequency(polls, item.getId().toString());
                status.put(item.getId(), count);
            }
            status.put(UNRECOMMENDED, Collections.frequency(polls, UNRECOMMENDED));
            long polled = redis.getItem(key, userId);
            response.addPollResponse(new PollResponse(status.values(), polled));
        }
        return response;
    }

    public void DBtoCache(Long postId) {
        String key = String.format(POLL_DEFAULT + "%s", postId);
        Entry entry = postRepository.getParticipants(postId);

        if (entry.getParticipants().equals("[null]")) {
            for (Participant participant : entry.getParticipants()) {
                redis.getPoll(key, participant.getUserId(), participant.getPollItemId());
            }
        }

        if (entry.getUnrecommended().equals("[null]")) {
            for (Unrecommended unrecommended : entry.getUnrecommended()) {
                redis.getPoll(key, unrecommended.getUserId(), UNRECOMMENDED);
            }
        }

        redis.expire(key, duration);
    }
}
