package com.nexters.buyornot.module.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueTokenReq {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
