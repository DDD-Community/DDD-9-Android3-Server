package com.nexters.buyornot.module.post.api.dto.response;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PollItemResponse {
    private Long id;
    private String itemUrl;
    private String brand;
    private String itemName;
    private String imgUrl;
    private int originalPrice;
    private int discountedRate;
    private int discountedPrice;

    public PollItemResponse (Long id, String brand, String itemUrl, String itemName, String imgUrl, BigDecimal originalPrice, int discountedRate, BigDecimal discountedPrice) {
        this.id = id;
        this.brand = brand;
        this.itemUrl = itemUrl;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.originalPrice = originalPrice.intValue();
        this.discountedRate = discountedRate;
        this.discountedPrice = discountedPrice.intValue();
    }

}
