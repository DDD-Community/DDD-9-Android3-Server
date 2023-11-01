package com.nexters.buyornot.module.item.dto.ably.sub;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Goods {

    private Integer sno;
    private String name;
    private String image;
    private String imageWebp;
    private String imageRatioWithOnePointTwo;
    private String webviewImageRatioWithOnePointTwo;
    private Boolean isOpen;
    private String deliveryType;
    private String skuCode;
    private Market market;
    private LinkedOption linkedOption;
    private Boolean isSoldout;
    private Boolean isBuyable;
    private Boolean isAccessibleDetail;
    private Boolean isNew;
    private Boolean isSale;
    private Integer sellCount;
    private Integer price;
    private Integer discountRate;
    private Integer totalReviewCount;
    private Integer positiveReviewCount;
    private Integer positiveReviewRate;
    private Boolean availableOnlyNewMember;
    private List<String> coverImages;
    private List<Object> hashTags;
    private Boolean isDeliveredByAbly;
    private List<String> optionNames;
    private Integer likesCount;
    private Integer deliveryFee;

}
