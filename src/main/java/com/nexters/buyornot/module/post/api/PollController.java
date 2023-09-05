package com.nexters.buyornot.module.post.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.global.config.resolver.LoginUser;
import com.nexters.buyornot.module.post.api.dto.response.PollResponse;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
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

    @Operation(summary = "투표", description = "비추천은 0으로 넘겨주시면 됩니다!")
    @PatchMapping("/poll")
    public ResponseEntity<ApiResponse<PollResponse>> takePoll(@LoginUser JwtUser user, @PathVariable(name = "postId") Long postId, @RequestParam(value = "choice") Long choice) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, pollService.takePoll(postId, user, choice));
    }
}
