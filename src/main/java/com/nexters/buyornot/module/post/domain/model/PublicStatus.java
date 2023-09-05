package com.nexters.buyornot.module.post.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublicStatus {
    PUBLIC("전체 공개"),
    PRIVATE("제한된 공개");

    String value;
}
