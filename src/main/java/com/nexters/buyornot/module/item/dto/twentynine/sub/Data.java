package com.nexters.buyornot.module.item.dto.twentynine.sub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "itemNo",
        "itemName",
        "frontBrandNo",
        "frontBrandNameKor",
        "frontBrandNameEng",
        "consumerPrice",
        "imageUrl",
        "heartCount",
        "reviewCount",
        "lastSalePercent",
        "lastSalePrice",
        "isSoldOut",
        "isFreeShipping",
        "isNew",
        "reviewAveragePoint",
        "heartOn",
        "subjectDescriptions",
        "colorHexes",
        "saleInfoV2"
})
public class Data {

    @JsonProperty("itemNo")
    public Integer itemNo;
    @JsonProperty("itemName")
    public String itemName;
    @JsonProperty("frontBrandNo")
    public Integer frontBrandNo;
    @JsonProperty("frontBrandNameKor")
    public String frontBrandNameKor;
    @JsonProperty("frontBrandNameEng")
    public String frontBrandNameEng;
    @JsonProperty("consumerPrice")
    public Integer consumerPrice;
    @JsonProperty("imageUrl")
    public String imageUrl;
    @JsonProperty("heartCount")
    public Integer heartCount;
    @JsonProperty("reviewCount")
    public Integer reviewCount;
    @JsonProperty("lastSalePercent")
    public Integer lastSalePercent;
    @JsonProperty("lastSalePrice")
    public Integer lastSalePrice;
    @JsonProperty("isSoldOut")
    public Boolean isSoldOut;
    @JsonProperty("isFreeShipping")
    public Boolean isFreeShipping;
    @JsonProperty("isNew")
    public Boolean isNew;
    @JsonProperty("reviewAveragePoint")
    public Float reviewAveragePoint;
    @JsonProperty("heartOn")
    public Boolean heartOn;
    @JsonProperty("subjectDescriptions")
    public List<String> subjectDescriptions;
    @JsonProperty("colorHexes")
    public List<Object> colorHexes;
    @JsonProperty("saleInfoV2")
    public SaleInfoV2 saleInfoV2;

}
