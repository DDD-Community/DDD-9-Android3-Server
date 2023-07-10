package com.nexters.buyornot.module.image.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    private String originFileName;

    private Long fileSize;

    private String fileType;

    private String imgUrl;
}
