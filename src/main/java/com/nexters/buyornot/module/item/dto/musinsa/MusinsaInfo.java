package com.nexters.buyornot.module.item.dto.musinsa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexters.buyornot.module.item.api.request.ItemRequest;
import com.nexters.buyornot.module.item.domain.ItemProvider;
import com.nexters.buyornot.module.item.dto.musinsa.sub.Data;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})
public class MusinsaInfo {
    @JsonProperty("data")
    public Data data;
}
