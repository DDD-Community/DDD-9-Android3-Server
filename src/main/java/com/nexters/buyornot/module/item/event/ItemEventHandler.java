package com.nexters.buyornot.module.item.event;

import com.nexters.buyornot.module.item.application.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ItemEventHandler {

    private final ItemService itemService;

    @EventListener
    public void saveItem(SavedItemEvent event) throws IOException {
        itemService.create(event.getItemUrls());
    }
}
