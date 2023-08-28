package com.nexters.buyornot.module.post.api.dto.response;

import lombok.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class PollResponse {
    int firstItem;
    int secondItem;
    int unrecommended;

    public PollResponse(Collection<Integer> values) {
        List<Integer> list = values.stream().collect(Collectors.toList());
        this.unrecommended = list.get(0);
        this.firstItem = list.get(1);
        this.secondItem = list.get(2);
    }
}
