package com.nexters.buyornot.module.post.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PollStatus {
    TEMPORARY, ONGOING, CLOSED
}
