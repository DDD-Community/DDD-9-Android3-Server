package com.nexters.buyornot.global.common.response;

import lombok.Builder;
import lombok.Getter;

/**
 * [공통] API Response 결과의 반환 값 관리
 */

@Getter
public class ApiResponse<T> {
    // API 응답 결과 Response
    private T result;

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
