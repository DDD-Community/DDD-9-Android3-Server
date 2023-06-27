package com.nexters.buyornot.module.auth.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetLoginRes {
    @Schema(description = "유저 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", type = "UUID")
    private UUID id;
    private TokenRes token;

    public static GetLoginRes of(final UUID userId, final TokenRes token) {
        return new GetLoginRes(userId, token);
    }
}
