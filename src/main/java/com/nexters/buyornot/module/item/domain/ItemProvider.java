package com.nexters.buyornot.module.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemProvider {

    MUSINSA("무신사"),
    ZIGZAG("지그재그"),
    APLUSB("29CM"),
    WCONCEPT("W컨셉"),
    ABLY("에이블리");

    private String value;
}
