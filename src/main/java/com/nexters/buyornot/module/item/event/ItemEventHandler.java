package com.nexters.buyornot.module.item.event;

import com.nexters.buyornot.module.item.application.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemEventHandler {

    private final ItemService itemService;
    List<String> urls = new ArrayList<>();

    @EventListener
    public void saveItem(SavedItemEvent event) throws IOException {
        urls.add(event.getItemUrl());
        urls.addAll(event.getItemUrls());

        itemService.create(urls);
    }
}
