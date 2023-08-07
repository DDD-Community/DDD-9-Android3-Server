package com.nexters.buyornot.module.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    // name : value
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    NON_MEMBER("NON_MEMBER");


    private String value;
}
