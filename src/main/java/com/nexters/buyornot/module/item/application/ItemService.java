package com.nexters.buyornot.module.item.application;

import com.nexters.buyornot.module.item.api.response.ItemResponse;
import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import com.nexters.buyornot.module.item.api.request.ItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CrawlingService crawlingService;

    @Transactional
    public void getInfo(List<String> itemUrls) throws IOException {
        for(String url: itemUrls) {
            if(url == null) continue;

            Item item = itemRepository.findByItemUrl(url)
                    .orElse(Item.newItem(crawlingService.of(url)));
            itemRepository.save(item);
            }
    }

    @Transactional
    public ItemResponse create(String url) throws IOException {
        Item item = itemRepository.findByItemUrl(url)
                .orElse(Item.newItem(crawlingService.of(url)));

        itemRepository.save(item);

        if(item.getUpdatedAt().compareTo(LocalDateTime.now()) > 7) {
            log.info("상품 정보를 업데이트합니다. " + url);
            ItemRequest newData = crawlingService.of(url);
            item.update(newData);
        }

        itemRepository.save(item);
        return item.newItemResponse();
    }
}
