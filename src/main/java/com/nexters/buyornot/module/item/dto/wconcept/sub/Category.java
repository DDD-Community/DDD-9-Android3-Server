package com.nexters.buyornot.module.item.dto.wconcept.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mediumCd",
        "mediumName",
        "categoryCd",
        "categoryDepthname1",
        "categoryDepthname2",
        "categoryDepthname3"
})
public class Category {

    @JsonProperty("mediumCd")
    private String mediumCd;
    @JsonProperty("mediumName")
    private String mediumName;
    @JsonProperty("categoryCd")
    private String categoryCd;
    @JsonProperty("categoryDepthname1")
    private String categoryDepthname1;
    @JsonProperty("categoryDepthname2")
    private String categoryDepthname2;
    @JsonProperty("categoryDepthname3")
    private String categoryDepthname3;
}
