package com.nexters.buyornot.module.archive.api.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ArchiveResponse {
    private Long id;
    private String userId;
    private String itemUrl;
    private String brand;
    private String itemName;
    private String imgUrl;
    private int originalPrice;
    private int discountedRate;
    private int discountedPrice;
    private boolean isLiked;
    private LocalDateTime updatedAt;

    public ArchiveResponse (Long id, String userId, String brand, String itemUrl, String itemName, String imgUrl, BigDecimal originalPrice, int discountedRate, BigDecimal discountedPrice, boolean isLiked, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.brand = brand;
        this.itemUrl = itemUrl;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.originalPrice = originalPrice.intValue();
        this.discountedRate = discountedRate;
        this.discountedPrice = discountedPrice.intValue();
        this.isLiked = isLiked;
        this.updatedAt = updatedAt;
    }
}
