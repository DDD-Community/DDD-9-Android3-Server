package com.nexters.buyornot.module.post.application;

import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import com.nexters.buyornot.module.item.event.SavedItemEvent;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.post.domain.PollItem;
import com.nexters.buyornot.module.post.domain.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.dto.response.PostResponse;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.nexters.buyornot.global.common.codes.ErrorCode.NOT_FOUND_ITEM_EXCEPTION;
import static com.nexters.buyornot.global.common.codes.ErrorCode.NOT_FOUND_POST_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PostResponse create(JwtUser jwtUser, CreatePostReq dto) {

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

    //글 작성자, 미참여자, 참여자 구분 필요
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_POST_EXCEPTION));

        PostResponse postResponse = post.newPostResponse();
        return postResponse;
    }

    //전체 공개 포스트만
    public List<PostResponse> getPage(final int page, final int count) {

        List<PostResponse> responseList = postRepository.findPageByPublicStatusOrderByIdDesc(PublicStatus.PUBLIC, PageRequest.of(page, count))
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
}
