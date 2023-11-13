package com.nexters.buyornot.module.item.dto.twentynine.sub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "consumerPrice",
        "sellPrice",
        "saleRate",
        "couponSaleRate",
        "isCoupon",
        "totalSellPrice",
        "totalSaleRate"
})
public class SaleInfoV2 {

    @JsonProperty("consumerPrice")
    public Integer consumerPrice;
    @JsonProperty("sellPrice")
    public Integer sellPrice;
    @JsonProperty("saleRate")
    public Integer saleRate;
    @JsonProperty("couponSaleRate")
    public Integer couponSaleRate;
    @JsonProperty("isCoupon")
    public Boolean isCoupon;
    @JsonProperty("totalSellPrice")
    public Integer totalSellPrice;
    @JsonProperty("totalSaleRate")
    public Integer totalSaleRate;

}
