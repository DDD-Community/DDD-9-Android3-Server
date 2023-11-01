package com.nexters.buyornot.module.item.dto.ably;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nexters.buyornot.module.item.dto.ably.sub.Goods;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AblyInfoDto {
    private Goods goods;
}