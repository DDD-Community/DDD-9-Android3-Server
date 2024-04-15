package com.nexters.buyornot.module.item.domain;

import com.nexters.buyornot.module.model.Price;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemInfo {
    Long id;
    String brand;
    String name;
    Price price;
    String itemUrl;
    String imgUrl;
}
