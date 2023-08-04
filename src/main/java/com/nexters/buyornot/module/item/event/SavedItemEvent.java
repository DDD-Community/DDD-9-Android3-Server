package com.nexters.buyornot.module.item.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SavedItemEvent {
    private String itemUrl;
    private List<String> itemUrls = new ArrayList<>();

    public SavedItemEvent(List<String> itemUrls) {
        this.itemUrls.addAll(itemUrls);
    }
    public SavedItemEvent(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public static SavedItemEvent of(List<String> itemUrls) {
        return new SavedItemEvent(itemUrls);
    }
    public static SavedItemEvent of(String itemUrl) {
        return new SavedItemEvent(itemUrl); }

}
