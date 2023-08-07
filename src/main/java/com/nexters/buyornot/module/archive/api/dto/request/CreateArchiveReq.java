package com.nexters.buyornot.module.archive.api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CreateArchiveReq {
    private Long itemId;
    private String itemUrl;
    private String brand;
    private String itemName;
    private String imgUrl;
    private int originalPrice;
    private int discountedRate;
    private int discountedPrice;
}
