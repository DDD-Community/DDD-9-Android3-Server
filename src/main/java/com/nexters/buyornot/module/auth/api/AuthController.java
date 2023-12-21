package com.nexters.buyornot.module.auth.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.global.config.resolver.LoginUser;
import com.nexters.buyornot.module.auth.api.dto.request.ReissueTokenReq;
import com.nexters.buyornot.module.auth.application.OAuthLoginService;
import com.nexters.buyornot.module.auth.api.dto.request.KakaoLoginParams;
import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.auth.application.TokenService;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthLoginService oAuthLoginService;
    private final TokenService tokenService;

    @Operation(summary = "로그인")
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<AuthTokens>> loginKakao(@RequestBody KakaoLoginParams params) {
        return ApiResponse.success(SuccessCode.LOGIN_SUCCESS, oAuthLoginService.login(params));
    }

    @Operation(summary = "reissue refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokens>> reissue(@Valid @RequestBody ReissueTokenReq request) {
        return ApiResponse.success(SuccessCode.REISSUE_SUCCESS, tokenService.reissueToken(request));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@LoginUser JwtUser user) {
        return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS, oAuthLoginService.logout(user));
    }

    @Operation(summary = "회원 탈퇴")
    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<String>> signOut(@LoginUser JwtUser user) {
        return ApiResponse.success(SuccessCode.SIGNOUT_SUCCESS, oAuthLoginService.signOut(user));
    }
}
