package com.nexters.buyornot.module.item.api.request;

import com.nexters.buyornot.module.item.domain.ItemProvider;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequest {

    private String brand;

    private String name;

    private String originalPrice;

    private String discountRate = "0";

    private double discountedPrice;

    private String imageUrl;

    private String itemUrl;

    @Enumerated(EnumType.STRING)
    private ItemProvider itemProvider;

    public static ItemRequest defaultConfig() {
        return builder()
                .build();
    }

    public static ItemRequest newItemDto(ItemProvider itemProvider, String brand, String itemName, String itemUrl, String imgUrl, String originPrice, String discountRate, double discountedPrice) {
        return builder()
                .itemProvider(itemProvider)
                .brand(brand)
                .name(itemName)
                .itemUrl(itemUrl)
                .imageUrl(imgUrl)
                .originalPrice(originPrice)
                .discountRate(discountRate)
                .discountedPrice(discountedPrice)
                .build();
    }

    public static ItemRequest updatedInfo(String brand, String name, String imgUrl, String originalPrice, String discountRate, double discountedPrice) {
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
