package com.nexters.buyornot.module.item.dto.wconcept;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexters.buyornot.module.item.dto.wconcept.sub.Category;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "itemCd",
        "brandCd",
        "mediumName",
        "categoryDepthname1",
        "categoryDepthname2",
        "categoryDepthname3",
        "brandNameKr",
        "itemNameFront",
        "itemNameBack",
        "recoType",
        "itemNameSub",
        "itemName",
        "itemTypeCd",
        "itemTypeName",
        "keyWords",
        "category",
        "imageName",
        "imageUrlDesktop",
        "imageUrlDesktopList",
        "imageUrlDesktopLarge",
        "imageUrlDesktopOrigin",
        "imageUrlMobile",
        "imageUrlMobileList",
        "imageUrlNewV2MobileList",
        "imageUrlNewMobileList",
        "final_discount_rate",
        "customerPrice",
        "brandNameEn",
        "vendorName",
        "finalPrice",
        "finalPriceDisplay",
        "synonym"
})
public class WconceptInfoDto {

    @JsonProperty("itemCd")
    private String itemCd;
    @JsonProperty("brandCd")
    private String brandCd;
    @JsonProperty("mediumName")
    private String mediumName;
    @JsonProperty("categoryDepthname1")
    private String categoryDepthname1;
    @JsonProperty("categoryDepthname2")
    private String categoryDepthname2;
    @JsonProperty("categoryDepthname3")
    private String categoryDepthname3;
    @JsonProperty("brandNameKr")
    private String brandNameKr;
    @JsonProperty("itemNameFront")
    private String itemNameFront;
    @JsonProperty("itemNameBack")
    private String itemNameBack;
    @JsonProperty("recoType")
    private Object recoType;
    @JsonProperty("itemNameSub")
    private String itemNameSub;
    @JsonProperty("itemName")
    private String itemName;
    @JsonProperty("itemTypeCd")
    private String itemTypeCd;
    @JsonProperty("itemTypeName")
    private String itemTypeName;
    @JsonProperty("keyWords")
    private List<Object> keyWords;
    @JsonProperty("category")
    private List<Category> category;
    @JsonProperty("imageName")
    private String imageName;
    @JsonProperty("imageUrlDesktop")
    private String imageUrlDesktop;
    @JsonProperty("imageUrlDesktopList")
    private String imageUrlDesktopList;
    @JsonProperty("imageUrlDesktopLarge")
    private String imageUrlDesktopLarge;
    @JsonProperty("imageUrlDesktopOrigin")
    private String imageUrlDesktopOrigin;
    @JsonProperty("imageUrlMobile")
    private String imageUrlMobile;
    @JsonProperty("imageUrlMobileList")
    private String imageUrlMobileList;
    @JsonProperty("imageUrlNewV2MobileList")
    private String imageUrlNewV2MobileList;
    @JsonProperty("imageUrlNewMobileList")
    private String imageUrlNewMobileList;
    @JsonProperty("final_discount_rate")
    private Float finalDiscountRate;
    @JsonProperty("customerPrice")
    private Float customerPrice;
    @JsonProperty("brandNameEn")
    private String brandNameEn;
    @JsonProperty("vendorName")
    private String vendorName;
    @JsonProperty("finalPrice")
    private Float finalPrice;
    @JsonProperty("finalPriceDisplay")
    private String finalPriceDisplay;
    @JsonProperty("synonym")
    private List<String> synonym;

}
