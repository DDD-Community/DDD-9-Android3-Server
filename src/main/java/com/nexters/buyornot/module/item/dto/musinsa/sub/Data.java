package com.nexters.buyornot.module.item.dto.musinsa.sub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "goodsNo",
        "goodsNm",
        "thumbnailImageUrl",
        "brand",
        "goodsPrice"
})
public class Data {
    @JsonProperty("goodsNo")
    public Integer goodsNo;
    @JsonProperty("goodsNm")
    public String goodsNm;
    @JsonProperty("thumbnailImageUrl")
    public String imgUrl;
    @JsonProperty("brand")
    public String brand;
    @JsonProperty("goodsPrice")
    public GoodsPrice goodsPrice;
}
