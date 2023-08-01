package com.nexters.buyornot.module.post.application;

import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.global.utils.RedisUtil;
import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import com.nexters.buyornot.module.item.event.SavedItemEvent;
import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.api.dto.response.PollResponse;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.domain.post.PollItem;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.nexters.buyornot.global.common.codes.ErrorCode.*;
import static com.nexters.buyornot.module.post.application.PollService.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisUtil redis;

    @Transactional
    public PostResponse create(JwtUser jwtUser, CreatePostReq dto) {

        if(jwtUser.equals(new JwtUser())) {
            new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);
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
        if(user.getName().equals(nonMember)) userId = postId + nonMember + LocalDateTime.now();
        else userId = user.getId().toString();

        String key = String.format(keyPrefix + "%s", postId);

        PostResponse response = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION))
                .newPostResponse();

        List<String> polls = redis.getPollsByPost(key);

        if(redis.alreadyPolled(key, userId)) {
            Map<String, Integer> status = new HashMap<>();

            for(PollItemResponse item : response.getPollItemResponseList()) {
                int count = Collections.frequency(polls, item.getId().toString());
                status.put(item.getId().toString(), count);
            }
            status.put(unRecommend, Collections.frequency(polls, unRecommend));

            response.addPollResponse(new PollResponse(status));
        }

        return response;
    }

    //전체 공개 포스트만
    public List<PostResponse> getPage(JwtUser user, final int page, final int count) {

        List<PostResponse> responseList = postRepository.findPageByPublicStatusOrderByIdDesc(PublicStatus.PUBLIC, PageRequest.of(page, count))
                .stream()
                .map(Post::newPostResponse)
                .collect(Collectors.toList());

        return responseList;
    }

    public List<PostResponse> getMine(JwtUser user, final int page, final int count) {

        List<PostResponse> responseList = postRepository.findPageByUser(user.getId(), PublicStatus.PUBLIC, PageRequest.of(page, count))
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

        return post.getId();
    }
}
