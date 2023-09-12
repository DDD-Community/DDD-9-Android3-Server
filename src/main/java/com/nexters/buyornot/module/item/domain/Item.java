package com.nexters.buyornot.module.item.domain;

import com.nexters.buyornot.module.archive.domain.Archive;
import com.nexters.buyornot.module.item.api.request.ItemRequest;
import com.nexters.buyornot.module.item.api.response.ItemResponse;
import com.nexters.buyornot.module.item.dto.UpdatedInfo;
import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Price;
import com.nexters.buyornot.module.post.domain.post.PollItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.nexters.buyornot.module.post.domain.post.PollItem.newPollItem;

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

    public static Item newItem(ItemRequest dto) {

        BigDecimal originalPrice = BigDecimal.valueOf(Double.valueOf(dto.getOriginalPrice()));
        BigDecimal discountedPrice = BigDecimal.valueOf(dto.getDiscountedPrice());

        Price price = new Price(originalPrice, dto.getDiscountRate(), discountedPrice);

        return Item.builder()
                .itemProvider(dto.getItemProvider())
                .brand(dto.getBrand())
                .name(dto.getName())
                .price(price)
                .itemUrl(dto.getItemUrl())
                .imgUrl(dto.getImageUrl())
                .build();
    }

    public PollItem createPollItem() {
        return newPollItem(this.id, this.brand, this.name, this.price, this.itemUrl, this.imgUrl);
    }

    public Archive newArchive(String userId) {
        return Archive.newArchive(userId, this.itemUrl, this.id, this.brand, this.name, this.imgUrl, this.price);
    }

    public void update(ItemRequest item) {
        BigDecimal originalPrice = BigDecimal.valueOf(Double.valueOf(item.getOriginalPrice()));
        BigDecimal discountedPrice = BigDecimal.valueOf(item.getDiscountedPrice());

        Price price = new Price(originalPrice, item.getDiscountRate(), discountedPrice);
        this.name = item.getName();
        this.imgUrl = item.getImageUrl();
        this.price = price;
    }

    public UpdatedInfo getUpdatedInfo() {
        return UpdatedInfo.updatedInfo(this.brand, this.name, this.imgUrl, this.price.getValue().toString(), String.valueOf(this.price.getDiscountRate()), this.price.getDiscountedPrice().doubleValue());
    }

    public ItemResponse newItemResponse() {
        return new ItemResponse(id, brand, name, imgUrl, itemUrl, getUpdatedAt());
    }
}
