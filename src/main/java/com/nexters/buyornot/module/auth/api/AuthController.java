package com.nexters.buyornot.module.auth.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.module.auth.application.OAuthLoginService;
import com.nexters.buyornot.module.auth.infra.KakaoLoginParams;
import com.nexters.buyornot.module.auth.model.AuthTokens;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthLoginService oAuthLoginService;

    @Operation(summary = "로그인")
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<AuthTokens>> loginKakao(@RequestBody KakaoLoginParams params) {
        return ApiResponse.success(SuccessCode.LOGIN_SUCCESS, oAuthLoginService.login(params));
    }
}
