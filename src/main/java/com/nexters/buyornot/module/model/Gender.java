package com.nexters.buyornot.module.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    F("FEMALE"),
    M("MALE");

    private String value;
}
