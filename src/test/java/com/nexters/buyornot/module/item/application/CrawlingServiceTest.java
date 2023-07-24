package com.nexters.buyornot.module.item.application;

import com.nexters.buyornot.module.item.dto.ItemDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest
class CrawlingServiceTest {

    @Autowired
    CrawlingService crawlingService;

    @Test
    @Transactional
    void crawling() throws IOException {
        System.out.println("==================start=======================");

        /**
         * 지그재그
         */
        ItemDto zigzag = crawlingService.of("https://zigzag.kr/catalog/products/113607837");
        System.out.println("brand: " + zigzag.getBrand() + " name: " + zigzag.getName());
        System.out.println("originalPrice: " + zigzag.getOriginalPrice() + " discountRate: " + zigzag.getDiscountRate() + " discountedPrice: " + zigzag.getDiscountedPrice());
        System.out.println("imgUrl: " + zigzag.getImageUrl());
        System.out.println("from: " + zigzag.getItemProvider().getValue());

        System.out.println("=========================================");

        /**
         * 무신사
         */
        ItemDto musinsa = crawlingService.of("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        System.out.println("brand: " + musinsa.getBrand() + " name: " + musinsa.getName());
        System.out.println("originalPrice: " + musinsa.getOriginalPrice() + " discountRate: " + musinsa.getDiscountRate() + " discountedPrice: " + musinsa.getDiscountedPrice());
        System.out.println("imgUrl: " + musinsa.getImageUrl());
        System.out.println("from: " + musinsa.getItemProvider().getValue());

        System.out.println("=========================================");

        /**
         * 29cm
         */

        ItemDto aplusb = crawlingService.of("https://product.29cm.co.kr/catalog/2142915");
        System.out.println("brand: " + aplusb.getBrand() + " name: " + aplusb.getName());
        System.out.println("originalPrice: " + aplusb.getOriginalPrice() + " discountRate: " + aplusb.getDiscountRate() + " discountedPrice: " + aplusb.getDiscountedPrice());
        System.out.println("imgUrl: " + aplusb.getImageUrl());
        System.out.println("from: " + aplusb.getItemProvider().getValue());

        System.out.println("=========================================");
    }

    @Test
    @Transactional
    void wconcept() throws IOException {

        //할인 O
        ItemDto w = crawlingService.of("https://www.wconcept.co.kr/Product/303147448");

        System.out.println("brand: " + w.getBrand() + " name: " + w.getName());
        System.out.println("originalPrice: " + w.getOriginalPrice() + " discountRate: " + w.getDiscountRate() + " discountedPrice: " + w.getDiscountedPrice());
        System.out.println("imgUrl: " + w.getImageUrl());
        System.out.println("from: " + w.getItemProvider().getValue());
    }

}