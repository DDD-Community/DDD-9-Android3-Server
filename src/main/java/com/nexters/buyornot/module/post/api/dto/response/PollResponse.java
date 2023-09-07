package com.nexters.buyornot.module.post.api.dto.response;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PollResponse {
    int firstItem;
    int secondItem;
    int unrecommended;
    long polled;

    public PollResponse(Collection<Integer> values, long polled) {
        List<Integer> list = values.stream().collect(Collectors.toList());
        if(!list.isEmpty()) {
            this.unrecommended = list.get(0);
            this.firstItem = list.get(1);
            this.secondItem = list.get(2);
            this.polled = polled;
        }
    }
}
