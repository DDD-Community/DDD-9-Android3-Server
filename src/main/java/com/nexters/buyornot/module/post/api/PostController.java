package com.nexters.buyornot.module.post.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.global.config.resolver.LoginUser;
import com.nexters.buyornot.module.post.application.PostService;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.user.dto.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "post", description = "게시물 API")
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 등록")
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<PostResponse>> writePoll(@LoginUser JwtUser user, @Validated @RequestBody CreatePostReq dto) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, postService.create(user, dto));
    }

    @Operation(summary = "게시물 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable(name = "postId") Long postId, @LoginUser JwtUser user) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getPost(user, postId));
    }

    @Operation(summary = "작성 글 목록 조회 - 투표 진행 중")
    @GetMapping("/ongoing-list")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getOngoing(@LoginUser JwtUser user, @RequestParam("page") final Integer page, @RequestParam("count") final int count) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getOngoing(user, page, count));
    }

    @Operation(summary = "작성 글 목록 조회 - 투표 마감")
    @GetMapping("/closed-list")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getClosed(@LoginUser JwtUser user, @RequestParam("page") final Integer page, @RequestParam("count") final int count) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getClosed(user, page, count));
    }

    @Operation(summary = "전체 게시글 리스트")
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<List<PostResponse>>> findPage(@LoginUser JwtUser user, @RequestParam("page") final Integer page, @RequestParam("count") final int count) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getPage(user, page, count));
    }

    @Operation(summary = "임시 저장 글 리스트")
    @GetMapping("/temporary")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getTemporaries(@LoginUser JwtUser user) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, postService.getTemporaries(user));
    }

    @Operation(summary = "글 수정")
    @PatchMapping("/{postId}/modification")
    public ResponseEntity<ApiResponse<PostResponse>> modifyPost(@LoginUser JwtUser user, @PathVariable(name = "postId") Long postId, @RequestBody CreatePostReq dto) {
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS, postService.updatePost(user, postId, dto));
    }

    @Operation(summary = "글 삭제")
    @PatchMapping("/{postId}/deletion")
    public ResponseEntity<ApiResponse<Long>> deletePost(@LoginUser JwtUser user, @PathVariable(name = "postId") Long postId) {
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS, postService.deletePost(user, postId));
    }

    @Operation(summary = "투표 종료")
    @PatchMapping("{/postId}/end-poll")
    public ResponseEntity<ApiResponse<PostResponse>> endPoll(@LoginUser JwtUser user, @PathVariable(name = "postId") Long postId) {
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS, postService.endPoll(user, postId));
    }
}
