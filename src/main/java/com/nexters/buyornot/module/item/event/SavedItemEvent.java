package com.nexters.buyornot.module.item.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SavedItemEvent {
    private List<String> itemUrls;

    public SavedItemEvent(List<String> itemUrls) {
        this.itemUrls = itemUrls;
    }

    public static SavedItemEvent of(List<String> itemUrls) {
        return new SavedItemEvent(itemUrls);
    }

}
