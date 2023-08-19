package com.nexters.buyornot.module.archive.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.global.config.resolver.LoginUser;
import com.nexters.buyornot.module.archive.api.dto.request.DeleteArchiveReq;
import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.application.ArchiveService;
import com.nexters.buyornot.module.model.EntityStatus;
import com.nexters.buyornot.module.user.dto.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "archive", description = "아카이브 API")
@RequestMapping("/api/archive")
@RequiredArgsConstructor
public class ArchiveController {

    private final ArchiveService archiveService;

    @Operation(summary = "웹에서 보관")
    @PostMapping("/from-web")
    public ResponseEntity<ApiResponse<ArchiveResponse>> fromWeb(@LoginUser JwtUser user, @RequestParam(name = "itemUrl") String itemUrl) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, archiveService.saveFromWeb(user, itemUrl));
    }

    @Operation(summary = "게시물에서 보관")
    @PostMapping("/from-post/{itemId}")
    public ResponseEntity<ApiResponse<ArchiveResponse>> fromPost(@LoginUser JwtUser user, @PathVariable(name = "itemId") Long itemId) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, archiveService.saveFromPost(user, itemId));
    }

    @Operation(summary = "아카이브 좋아요")
    @PatchMapping("/pick/{archiveId}")
    public ResponseEntity<ApiResponse<ArchiveResponse>> likeArchive(@LoginUser JwtUser user, @PathVariable(name = "archiveId") Long archiveId) {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, archiveService.likeArchive(user, archiveId));
    }

    @Operation(summary = "내 아카이브함")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ArchiveResponse>>> getAll(@LoginUser JwtUser user, @RequestParam("page") final Integer page, @RequestParam("count") final int count) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, archiveService.getAll(user, page, count));
    }

    @Operation(summary = "내 아카이브함 - 좋아요")
    @GetMapping("/liked-list")
    public ResponseEntity<ApiResponse<List<ArchiveResponse>>> getLikes(@LoginUser JwtUser user, @RequestParam("page") final Integer page, @RequestParam("count") final int count) {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, archiveService.getLikes(user, page, count));
    }

    @Operation(summary = "아카이브 삭제")
    @PatchMapping("/deletion")
    public ResponseEntity<ApiResponse<String>> deleteArchive(@LoginUser JwtUser user, @RequestBody DeleteArchiveReq deleteArchiveReq) {
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS, archiveService.delete(user, deleteArchiveReq));
    }
}
