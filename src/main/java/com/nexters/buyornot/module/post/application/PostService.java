package com.nexters.buyornot.module.post.application;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.global.utils.RedisUtil;
import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import com.nexters.buyornot.module.item.event.SavedItemEvent;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.post.api.dto.request.FromArchive;
import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.api.dto.response.PollResponse;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.dao.poll.ParticipantRepository;
import com.nexters.buyornot.module.post.dao.poll.UnrecommendedRepository;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.poll.Participant;
import com.nexters.buyornot.module.post.domain.poll.Unrecommended;
import com.nexters.buyornot.module.post.domain.post.PollItem;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.nexters.buyornot.global.common.codes.ErrorCode.*;
import static com.nexters.buyornot.global.common.constant.RedisKey.POLL_DEFAULT;
import static com.nexters.buyornot.module.post.application.PollService.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final ParticipantRepository participantRepository;
    private final UnrecommendedRepository unrecommendedRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisUtil redis;

    @Transactional
    public PostResponse create(JwtUser jwtUser, CreatePostReq dto) {

        if(jwtUser.getRole().equals(Role.NON_MEMBER.getValue())) throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        if(dto.getPublicStatus().equals(PublicStatus.TEMPORARY_STORAGE)) {
            List<Post> temporaryList = postRepository.findByUserIdAndPublicStatus(jwtUser.getId(), PublicStatus.TEMPORARY_STORAGE);
            if(temporaryList.size() >= 5) {
                throw new BusinessExceptionHandler(STORAGE_COUNT_EXCEEDED);
            }
        }

        eventPublisher.publishEvent(
                SavedItemEvent.of(dto.getItemUrls())
        );

        List<PollItem> pollItems = addPollItem(dto.getItemUrls());

        Post post = Post.newPost(jwtUser, dto, pollItems);
        Post savedPost = postRepository.save(post);
        PostResponse postResponse = savedPost.newPostResponse();
        return postResponse;

    }

    private List<PollItem> addPollItem(List<String> itemUrls) {

        List<PollItem> pollItems = new ArrayList<>();

        for(String url : itemUrls) {
            Item item = itemRepository.findByItemUrl(url)
                    .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ITEM_EXCEPTION));
            pollItems.add(item.createPollItem());
        }

        return pollItems;
    }

    public PostResponse getPost(JwtUser user, Long postId) {
        String userId;
        //비회원
        if(user.getRole().equals(Role.NON_MEMBER.getValue())) userId = postId + NON_MEMBER + LocalDateTime.now();
        else userId = user.getId().toString();

        String key = String.format(POLL_DEFAULT + "%s", postId);

        PostResponse response = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION))
                .newPostResponse();

        if(!redis.exists(key)) DBtoCache(postId, postRepository.findById(postId).get().getItemList());

        List<Long> polls = redis.getPollsByPost(key);

        if(redis.alreadyPolled(key, userId)) {
            Map<Long, Integer> status = new HashMap<>();

            for(PollItemResponse item : response.getPollItemResponseList()) {
                int count = Collections.frequency(polls, item.getId().toString());
                status.put(item.getId(), count);
            }
            status.put(UNRECOMMENDED, Collections.frequency(polls, UNRECOMMENDED));
            response.addPollResponse(new PollResponse(status));
        }
        return response;
    }

    @Transactional
    public String endPoll(JwtUser user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));
        if(!post.checkValidity(user.getId())) throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        post.endPoll();
        postRepository.save(post);
        return SuccessCode.DELETE_SUCCESS.getMessage();
    }

    //전체 공개 포스트만
    public List<PostResponse> getPage(JwtUser user, final int page, final int count) {
        String userId;
        //비회원
        if(user.getRole().equals(Role.NON_MEMBER.getValue())) userId = NON_MEMBER + LocalDateTime.now();
        else userId = user.getId().toString();

        List<PostResponse> responseList = postRepository.findPageByPublicStatusOrderByIdDesc(PublicStatus.PUBLIC, PageRequest.of(page, count))
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());

        for(PostResponse response : responseList) {
            Long postId = response.getId();
            String key = String.format(POLL_DEFAULT + "%s", postId);

            if(!redis.exists(key)) DBtoCache(postId, postRepository.findById(postId).get().getItemList());

            List<Long> polls = redis.getPollsByPost(key);

            if(redis.alreadyPolled(key, userId)) {
                Map<Long, Integer> status = new HashMap<>();

                for(PollItemResponse item : response.getPollItemResponseList()) {
                    int pollCount = Collections.frequency(polls, item.getId());
                    status.put(item.getId(), pollCount);
                }

                status.put(UNRECOMMENDED, Collections.frequency(polls, UNRECOMMENDED));
                response.addPollResponse(new PollResponse(status));
            }
        }
        return responseList;
    }

    public List<PostResponse> getOngoing(JwtUser user, final int page, final int count) {
        List<PostResponse> responseList = postRepository.findPageByUserAndPollStatus(user.getId(), PollStatus.ONGOING, PageRequest.of(page, count))
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());

        return responseList;
    }

    public List<PostResponse> getClosed(JwtUser user, final int page, final int count) {
        List<PostResponse> responseList = postRepository.findPageByUserAndPollStatus(user.getId(), PollStatus.CLOSED, PageRequest.of(page, count))
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());

        return responseList;
    }

    public List<PostResponse> getTemporaries(JwtUser user) {

        List<PostResponse> responseList = postRepository.findTemporaries(user.getId(), PublicStatus.TEMPORARY_STORAGE)
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());

        return responseList;
    }

    @Transactional
    public PostResponse updatePost(JwtUser user, Long postId, CreatePostReq dto) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));

        if (!post.checkValidity(user.getId())) throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        eventPublisher.publishEvent(
                SavedItemEvent.of(dto.getItemUrls())
        );

        List<PollItem> pollItems = addPollItem(dto.getItemUrls());

        post.update(dto, pollItems);

        return post.newPostResponse();
    }

    @Transactional
    public Long deletePost(JwtUser user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));

        if (!post.checkValidity(user.getId())) throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        post.delete();
        postRepository.save(post);

        return post.getId();
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
    public PostResponse createFromArchive(JwtUser user, Long itemId1, Long itemId2, FromArchive dto) {
        Item item1 = itemRepository.findById(itemId1)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ITEM_EXCEPTION));
        Item item2 = itemRepository.findById(itemId2)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ITEM_EXCEPTION));

        List<PollItem> pollItems = new ArrayList<>();
        pollItems.add(item1.createPollItem());
        pollItems.add(item2.createPollItem());

        Post post = Post.newPostFromArchive(user, dto, pollItems);
        Post savedPost = postRepository.save(post);
        PostResponse postResponse = savedPost.newPostResponse();
        return postResponse;
    }
}
