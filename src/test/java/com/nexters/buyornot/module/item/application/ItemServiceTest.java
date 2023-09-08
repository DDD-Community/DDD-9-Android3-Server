package com.nexters.buyornot.module.item.application;

import com.nexters.buyornot.module.item.api.response.ItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@Slf4j
@SpringBootTest
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Test
    @Transactional
    void 아이템_정보() throws IOException {
        String url = "https://www.musinsa.com/app/goods/2454238";
//        String url = "https://smartstore.naver.com/xoancjs/products/4877035962?NaPm=ct%3Dlmae0thc%7Cci%3Dd1fb9eafb1f83816dd9ebe38628a1d1514331dc8%7Ctr%3Dsls%7Csn%3D1115043%7Chk%3Dc1c7738d7b9786863e5932bb5dd08018dfe8ba8a";
        ItemResponse response1 = itemService.create(url);
        ItemResponse response2 = itemService.create(url);

        log.info(response2.getImageUrl());
        assertThat(response2.getUpdatedAt()).isEqualTo(response1.getUpdatedAt());
    }
}
