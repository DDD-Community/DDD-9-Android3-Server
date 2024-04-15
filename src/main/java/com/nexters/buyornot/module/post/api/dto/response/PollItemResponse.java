package com.nexters.buyornot.module.post.api.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PollItemResponse {
    private Long id;
    private Long itemId;
    private String itemUrl;
    private String brand;
    private String itemName;
    private String imgUrl;
    private int originalPrice;
    private int discountedRate;
    private int discountedPrice;
    private boolean isLiked = false;

//    public PollItemResponse() {
//        this.isLiked = false;
//    }

    public PollItemResponse(Long id, Long itemId, String brand, String itemUrl, String itemName, String imgUrl,
                            BigDecimal originalPrice, int discountedRate, BigDecimal discountedPrice) {
        this.id = id;
        this.itemId = itemId;
        this.brand = brand;
        this.itemUrl = itemUrl;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.originalPrice = originalPrice.intValue();
        this.discountedRate = discountedRate;
        this.discountedPrice = discountedPrice.intValue();
    }

    public PollItemResponse(Long id, Long itemId, String brand, String itemUrl, String itemName, String imgUrl,
                            BigDecimal originalPrice, int discountedRate, BigDecimal discountedPrice, boolean isLiked) {
        this.id = id;
        this.itemId = itemId;
        this.brand = brand;
        this.itemUrl = itemUrl;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.originalPrice = originalPrice.intValue();
        this.discountedRate = discountedRate;
        this.discountedPrice = discountedPrice.intValue();
        this.isLiked = !isLiked;
    }

    public void addArchiveStatus() {
        isLiked = true;
    }
}
