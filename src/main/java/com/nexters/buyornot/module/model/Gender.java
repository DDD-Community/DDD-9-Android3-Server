package com.nexters.buyornot.module.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    F("female"),
    M("male");

    private String value;
}
