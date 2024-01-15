package com.nexters.buyornot.module.item.dto.musinsa.sub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "minPrice",
        "maxPrice",
        "originPrice",
        "discountRate",
        "isSale"
})
public class GoodsPrice {
    @JsonProperty("minPrice")
    public Integer minPrice;
    @JsonProperty("maxPrice")
    public Integer maxPrice;
    @JsonProperty("originPrice")
    public Integer originPrice;
    @JsonProperty("discountRate")
    public Integer discountRate;
    @JsonProperty("isSale")
    public String isSale;
}
