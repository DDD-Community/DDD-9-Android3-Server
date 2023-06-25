package com.nexters.buyornot.module.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    FEMALE("F"),
    MALE("M");

    private String value;
}
