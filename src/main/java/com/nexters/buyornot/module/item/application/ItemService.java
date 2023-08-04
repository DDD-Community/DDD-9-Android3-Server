package com.nexters.buyornot.module.item.application;

import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import com.nexters.buyornot.module.item.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    public void create(List<String> itemUrls) throws IOException {

        for(String url: itemUrls) {
            Optional<Item> item = itemRepository.findByItemUrl(url);

            if(item.isEmpty()) {
                log.info("해당 url의 상품은 존재하지 않습니다. " + url);
                Item newItem = Item.newItem(crawlingService.of(url));
                itemRepository.save(newItem);
            } else {
                /**
                 * 이미 존재한다면 상품 정보 update
                 */
                log.info("해당 url의 상품 정보를 업데이트합니다. " + url);

                ItemDto newData = crawlingService.of(url);
                item.get().update(newData);
                itemRepository.save(item.get());
            }
        }
    }
}
