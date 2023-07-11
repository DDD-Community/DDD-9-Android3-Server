package com.nexters.buyornot.global.common.response;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * [공통] API Response 결과의 반환 값 관리
 */

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    // API 응답 결과 Response
    private T result;

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

//    @Builder
//    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
//        this.result = result;
//        this.resultCode = resultCode;
//        this.resultMsg = resultMsg;
//    }


    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode, T data) {
        //        ApiResponse ar = ApiResponse.builder()
//                .result("health check OK")
//                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
//                .resultMsg(SuccessCode.SELECT_SUCCESS.getCode())
//                .build();
//        return new ResponseEntity<>(ar, HttpStatus.OK);



        return ResponseEntity
                .status(successCode.getStatus())
                .body(new ApiResponse<>(data, successCode.getStatus(), successCode.getMessage()));
    }
}
