package com.nexters.buyornot.module.post.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.global.config.resolver.LoginUser;
import com.nexters.buyornot.module.post.api.dto.response.PollResponse;
import com.nexters.buyornot.module.post.application.PollService;
import com.nexters.buyornot.module.user.dto.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/{postId}")
public class PollController {

    private final PollService pollService;

    @Operation(summary = "투표", description = "poll item id를 String 타입으로 전환해서 넘겨주세요! 비추천이 기본값입니다.")
    @PatchMapping("/poll")
    public ResponseEntity<ApiResponse<PollResponse>> takePoll(@LoginUser JwtUser user, @PathVariable(name = "postId") Long postId, @RequestParam(value = "choice") Long poll) {
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS, pollService.takePoll(postId, user, poll));
    }
}
