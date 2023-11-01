package com.nexters.buyornot.module.item.application;

import com.nexters.buyornot.module.item.api.request.ItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest
class CrawlingServiceTest {

    @Autowired
    CrawlingService crawlingService;

    @Test
    @Transactional
    void crawling() throws IOException, URISyntaxException {
        System.out.println("==================start=======================");

        /**
         * 지그재그
         */
        ItemRequest zigzag = crawlingService.of("https://zigzag.kr/catalog/products/113607837");
        System.out.println("brand: " + zigzag.getBrand() + " name: " + zigzag.getName());
        System.out.println("originalPrice: " + zigzag.getOriginalPrice() + " discountRate: " + zigzag.getDiscountRate() + " discountedPrice: " + zigzag.getDiscountedPrice());
        System.out.println("imgUrl: " + zigzag.getImageUrl());
        System.out.println("from: " + zigzag.getItemProvider().getValue());

        System.out.println("=========================================");

        /**
         * 무신사
         */
        ItemRequest musinsa = crawlingService.of("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        System.out.println("brand: " + musinsa.getBrand() + " name: " + musinsa.getName());
        System.out.println("originalPrice: " + musinsa.getOriginalPrice() + " discountRate: " + musinsa.getDiscountRate() + " discountedPrice: " + musinsa.getDiscountedPrice());
        System.out.println("imgUrl: " + musinsa.getImageUrl());
        System.out.println("from: " + musinsa.getItemProvider().getValue());

        System.out.println("=========================================");

        /**
         * 29cm
         */

        ItemRequest aplusb = crawlingService.of("https://product.29cm.co.kr/catalog/2142915");
        System.out.println("brand: " + aplusb.getBrand() + " name: " + aplusb.getName());
        System.out.println("originalPrice: " + aplusb.getOriginalPrice() + " discountRate: " + aplusb.getDiscountRate() + " discountedPrice: " + aplusb.getDiscountedPrice());
        System.out.println("imgUrl: " + aplusb.getImageUrl());
        System.out.println("from: " + aplusb.getItemProvider().getValue());

        System.out.println("=========================================");
    }

    @Test
    @Transactional
    void wconcept() throws IOException, URISyntaxException {

        //할인 O
        ItemRequest onSale = crawlingService.of("https://www.wconcept.co.kr/Product/303147448");

        System.out.println("brand: " + onSale.getBrand() + " name: " + onSale.getName());
        System.out.println("originalPrice: " + onSale.getOriginalPrice() + " discountRate: " + onSale.getDiscountRate() + " discountedPrice: " + onSale.getDiscountedPrice());
        System.out.println("imgUrl: " + onSale.getImageUrl());
        System.out.println("from: " + onSale.getItemProvider().getValue());

        System.out.println("=========================================");

        //할인 X

        ItemRequest w = crawlingService.of("https://www.wconcept.co.kr/Product/303137901");

        System.out.println("brand: " + w.getBrand() + " name: " + w.getName());
        System.out.println("originalPrice: " + w.getOriginalPrice() + " discountRate: " + w.getDiscountRate() + " discountedPrice: " + w.getDiscountedPrice());
        System.out.println("imgUrl: " + w.getImageUrl());
        System.out.println("from: " + w.getItemProvider().getValue());

    }

    @Test
    @Transactional
    void ably() throws IOException, URISyntaxException {
        ItemRequest item = crawlingService.of("https://m.a-bly.com/goods/8167148");

        System.out.println("brand: " + item.getBrand() + " name: " + item.getName());
        System.out.println("originalPrice: " + item.getOriginalPrice() + " discountRate: " + item.getDiscountRate() + " discountedPrice: " + item.getDiscountedPrice());
        System.out.println("imgUrl: " + item.getImageUrl());
        System.out.println("from: " + item.getItemProvider().getValue());
    }

    @Test
    @DisplayName("Ably Json Crawling Test")
    public void testAblyJson() {
        // given
        String url = "https://m.a-bly.com/goods/11145152?param1=value1&param2=value2";
        // when

        // then
        try {
            var result = crawlingService.getAblyJson(url);
            System.out.println(result.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Wconcept Json Crawling Test")
    public void testWconceptJson() {
        // given
        String url = "https://www.wconcept.co.kr/Product/303804583";
        // when

        // then
        try {
            var result = crawlingService.getWConceptJson(url);
            System.out.println(result.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("29cm Json Crawling Test")
    public void test29CMJson() {
        // given
        String uri = "https://product.29cm.co.kr/catalog/1696690?source=collection&source_content_no=1552&source_content_title=29CM%EC%97%90%EC%84%9C+%EC%B2%98%EC%9D%8C+%EB%A7%8C%EB%82%98%EB%8A%94+%EB%8B%B9%EC%8B%A0%EC%9D%98+%EC%B7%A8%ED%96%A5";

        // when

        // then
        try {
            var result = crawlingService.get29cmJson(uri);
            System.out.println(result);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}