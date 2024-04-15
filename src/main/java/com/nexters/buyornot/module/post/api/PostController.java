package com.nexters.buyornot.module.post.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.global.config.resolver.LoginUser;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.request.FromArchive;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.application.PostService;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "post", description = "게시물 API")
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 등록")
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<PostResponse>> create(@LoginUser JwtUser user,
                                                            @Valid @RequestBody CreatePostReq dto) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, postService.create(user, dto));
    }

    @Operation(summary = "임시저장 글 출간")
    @PostMapping("/publish-post/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> publish(@LoginUser JwtUser user,
                                                             @PathVariable(name = "postId") Long postId,
                                                             @Valid @RequestBody CreatePostReq dto) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, postService.publish(user, postId, dto));
    }

    @Operation(summary = "아카이브에서 글 작성")
    @PostMapping("/from-archive")
    public ResponseEntity<ApiResponse<PostResponse>> createFromArchive(@LoginUser JwtUser user,
                                                                       @RequestParam Long itemId1,
                                                                       @RequestParam Long itemId2,
                                                                       @Valid @RequestBody FromArchive dto) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS,
                postService.createFromArchive(user, itemId1, itemId2, dto));
    }

    @Operation(summary = "게시물 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable(name = "postId") Long postId,
                                                             @LoginUser JwtUser user) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getPost(user, postId));
    }

    @Operation(summary = "작성 글 목록 조회")
    @GetMapping("/ongoing-list")
    public ResponseEntity<ApiResponse<Slice<PostResponse>>> getOngoing(@LoginUser JwtUser user,
                                                                       @RequestParam("is_ongoing") boolean isOngoing,
                                                                       @RequestParam("page") final Integer page,
                                                                       @RequestParam("count") final int count) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getMyPosts(user, isOngoing, page, count));
    }

    @Operation(summary = "전체 게시글 리스트")
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<Slice<PostResponse>>> findPage(@LoginUser JwtUser user,
                                                                     @RequestParam("page") final Integer page,
                                                                     @RequestParam("count") final int count) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getMain(user, page, count));
    }

    @Operation(summary = "임시 저장 글 리스트")
    @GetMapping("/temporary")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getTemporaries(@LoginUser JwtUser user) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getTemporaries(user));
    }

    @Operation(summary = "글 수정")
    @PatchMapping("/{postId}/modification")
    public ResponseEntity<ApiResponse<PostResponse>> modifyPost(@LoginUser JwtUser user,
                                                                @PathVariable(name = "postId") Long postId,
                                                                @Valid @RequestBody CreatePostReq dto) {
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS, postService.updatePost(user, postId, dto));
    }

    @Operation(summary = "글 삭제")
    @PatchMapping("/{postId}/deletion")
    public ResponseEntity<ApiResponse<Long>> deletePost(@LoginUser JwtUser user,
                                                        @PathVariable(name = "postId") Long postId) {
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS, postService.deletePost(user, postId));
    }

    @Operation(summary = "투표 종료")
    @PatchMapping("{postId}/end-poll")
    public ResponseEntity<ApiResponse<String>> endPoll(@LoginUser JwtUser user,
                                                       @PathVariable(name = "postId") Long postId) {
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS, postService.endPoll(user, postId));
    }
}
