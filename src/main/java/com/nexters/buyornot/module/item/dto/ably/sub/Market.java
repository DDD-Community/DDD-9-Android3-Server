package com.nexters.buyornot.module.item.dto.ably.sub;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Market {

    private Integer sno;
    private String name;
    private Object introduce;
    private String instagram;
    private String hashTag;
    private List<String> hashTagList;
    private List<String> ageTags;
    private List<String> styleTags;
    private String badge;
    private String image;
    private String banner;

}
