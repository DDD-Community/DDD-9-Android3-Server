package com.nexters.buyornot.module.item.dto;

import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatedInfo {

    private String brand;

    private String name;

    private String originalPrice;

    private String discountRate;

    private double discountedPrice;

    private String imageUrl;


    public static UpdatedInfo updatedInfo(String brand, String name, String imgUrl, String originalPrice, String discountRate, double discountedPrice) {
        return builder()
                .brand(brand)
                .name(name)
                .imageUrl(imgUrl)
                .originalPrice(originalPrice)
                .discountRate(discountRate)
                .discountedPrice(discountedPrice)
                .build();
    }
}
