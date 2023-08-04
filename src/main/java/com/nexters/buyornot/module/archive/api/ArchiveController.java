package com.nexters.buyornot.module.archive.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.global.config.resolver.LoginUser;
import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.application.ArchiveService;
import com.nexters.buyornot.module.user.dto.JwtUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "archive", description = "아카이브 API")
@RequestMapping("/api/archive")
@RequiredArgsConstructor
public class ArchiveController {

    private final ArchiveService archiveService;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<ArchiveResponse>> storeItem(@LoginUser JwtUser user, @RequestParam(name = "url") String url) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, archiveService.save(user, url));
    }



}
