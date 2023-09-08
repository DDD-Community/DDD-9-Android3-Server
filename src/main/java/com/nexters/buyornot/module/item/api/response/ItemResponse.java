package com.nexters.buyornot.module.item.api.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@Setter
public class ItemResponse {
    private Long id;
    private String brand;
    private String name;
    private String imageUrl;
    private String itemUrl;
    private LocalDateTime updatedAt;

    public ItemResponse(Long id, String brand, String name, String imageUrl, String itemUrl, LocalDateTime updatedAt) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.imageUrl = imageUrl;
        this.itemUrl = itemUrl;
        this.updatedAt = updatedAt;
    }
}
