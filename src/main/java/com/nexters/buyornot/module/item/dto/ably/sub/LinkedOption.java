package com.nexters.buyornot.module.item.dto.ably.sub;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LinkedOption {

    private Integer sno;
    private List<String> optionNames;
    private Integer originalPrice;
    private Integer price;
    private Integer point;
    private Boolean isSoldout;

}
