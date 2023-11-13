package com.nexters.buyornot.module.item.dto.twentynine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexters.buyornot.module.item.dto.twentynine.sub.Data;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})
public class TwentyNineInfoDto {
    @JsonProperty("data")
    public Data data;
}

