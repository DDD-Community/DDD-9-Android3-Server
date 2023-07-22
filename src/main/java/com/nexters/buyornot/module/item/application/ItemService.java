package com.nexters.buyornot.module.item.application;

import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CrawlingService crawlingService;

    public void create(List<String> itemUrls) throws IOException {

        for(String url: itemUrls) {
            Optional<Item> item = itemRepository.findByItemUrl(url);

            if(item.isEmpty()) {
                Item newItem = Item.newItem(crawlingService.of(url));
                itemRepository.save(newItem);
            }
        }
    }
}
