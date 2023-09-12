package com.nexters.buyornot.module.item.api;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.module.item.api.response.ItemResponse;
import com.nexters.buyornot.module.item.application.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Tag(name = "item", description = "상품 API")
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @Operation(summary = "상품 정보")
    @GetMapping
    public ResponseEntity<ApiResponse<ItemResponse>> create(@RequestParam("url") String url) throws IOException {
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS, itemService.create(url));
    }

}
