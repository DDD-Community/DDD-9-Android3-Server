package com.nexters.buyornot.module.post.api.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class PollResponse {
    Map<Long, Integer> result;

    public PollResponse(Map<Long, Integer> status) {
        this.result = status;
    }
}
