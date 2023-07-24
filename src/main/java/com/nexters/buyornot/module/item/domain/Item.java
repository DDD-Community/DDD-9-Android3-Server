package com.nexters.buyornot.module.item.domain;

import com.nexters.buyornot.module.item.dto.ItemDto;
import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Price;
import com.nexters.buyornot.module.post.domain.PollItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.nexters.buyornot.module.post.domain.PollItem.newPollItem;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String brand;

    private String name;

    @Embedded
    private Price price;

    @Lob
    private String itemUrl;

    @Lob
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private ItemProvider itemProvider;

    public static Item newItem(ItemDto dto) {

        BigDecimal originalPrice = BigDecimal.valueOf(Double.valueOf(dto.getOriginalPrice()));
        BigDecimal discountedPrice = BigDecimal.valueOf(dto.getDiscountedPrice());

        Price price = new Price(originalPrice, dto.getDiscountRate(), discountedPrice);

        return Item.builder()
                .itemProvider(dto.getItemProvider())
                .brand(dto.getBrand())
                .price(price)
                .itemUrl(dto.getItemUrl())
                .imgUrl(dto.getImageUrl())
                .build();
    }

    public PollItem createPollItem() {
        return newPollItem(this.name, this.price, this.itemUrl, this.imgUrl);
    }
}
