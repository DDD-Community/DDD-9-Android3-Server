package com.nexters.buyornot.module.archive.domain;

import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.item.dto.UpdatedInfo;
import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Price;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "entity_status='ACTIVE'")
public class Archive extends BaseEntity {

    @Id
    @Column(name = "archive_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemUrl;

    @Column(length = 255)
    private String userId;

    private Long itemId;

    private String brand;

    private String itemName;

    @Embedded
    private Price price;

    @Lob
    private String imgUrl;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isLiked;

    private Archive (String userId, String itemUrl, Long itemId, String brand, String itemName, String img, Price price) {
        this.userId = userId;
        this.itemUrl = itemUrl;
        this.itemId = itemId;
        this.brand = brand;
        this.itemName = itemName;
        this.imgUrl = img;
        this.price = price;
    }

    public static Archive newArchive(String userId, String itemUrl, Long itemId, String brand, String itemName, String img, Price price) {
        return new Archive(userId, itemUrl, itemId, brand, itemName, img, price);}

    public ArchiveResponse newResponse() {
        return new ArchiveResponse(this.id, this.userId, this.itemId, this.brand, this.itemUrl, this.itemName, this.imgUrl, this.price.getValue(), this.price.getDiscountRate(), this.price.getDiscountedPrice(), this.isLiked, this.getUpdatedAt());
    }

    public void update(UpdatedInfo dto) {
        BigDecimal originalPrice = BigDecimal.valueOf(Double.valueOf(dto.getOriginalPrice()));
        BigDecimal discountedPrice = BigDecimal.valueOf(dto.getDiscountedPrice());
        Price price = new Price(originalPrice, dto.getDiscountRate(), discountedPrice);

        this.brand = dto.getBrand() + "1";
        this.itemName = dto.getName();
        this.imgUrl = dto.getImageUrl();
        this.price = price;
    }

    public void like() {
        if(isLiked == true) {
            isLiked = false;
            return;
        }
        this.isLiked = true;
    }

    public boolean verifyValidity(String userId) {
        if(this.userId.equals(userId)) return true;
        return false;
    }

    public Long getId() {
        return id;
    }
}
