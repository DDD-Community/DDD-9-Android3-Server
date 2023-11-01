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
    @DisplayName("29cm 크롤링 테스트")
    @Transactional
    public void aplusb() throws IOException, URISyntaxException {
        ItemRequest aplusb = crawlingService.of("https://product.29cm.co.kr/catalog/2142915");
        System.out.println("brand: " + aplusb.getBrand() + " name: " + aplusb.getName());
        System.out.println("originalPrice: " + aplusb.getOriginalPrice() + " discountRate: " + aplusb.getDiscountRate() + " discountedPrice: " + aplusb.getDiscountedPrice());
        System.out.println("imgUrl: " + aplusb.getImageUrl());
        System.out.println("from: " + aplusb.getItemProvider().getValue());

        System.out.println("=========================================");
    }

    @Test
    @DisplayName("무신사 크롤링 테스트")
    @Transactional
    public void musinsa() throws IOException, URISyntaxException {
        ItemRequest musinsa = crawlingService.of("https://www.musinsa.com/app/goods/3404788?loc=goods_rank");
        System.out.println("brand: " + musinsa.getBrand() + " name: " + musinsa.getName());
        System.out.println("originalPrice: " + musinsa.getOriginalPrice() + " discountRate: " + musinsa.getDiscountRate() + " discountedPrice: " + musinsa.getDiscountedPrice());
        System.out.println("imgUrl: " + musinsa.getImageUrl());
        System.out.println("from: " + musinsa.getItemProvider().getValue());

        System.out.println("=========================================");
    }

    @Test
    @DisplayName("Zigzag 크롤링 테스트")
    @Transactional
    public void zigzag() throws IOException, URISyntaxException {
        ItemRequest zigzag = crawlingService.of("https://zigzag.kr/catalog/products/113607837");
        System.out.println("brand: " + zigzag.getBrand() + " name: " + zigzag.getName());
        System.out.println("originalPrice: " + zigzag.getOriginalPrice() + " discountRate: " + zigzag.getDiscountRate() + " discountedPrice: " + zigzag.getDiscountedPrice());
        System.out.println("imgUrl: " + zigzag.getImageUrl());
        System.out.println("from: " + zigzag.getItemProvider().getValue());

        System.out.println("=========================================");
    }

    @Test
    @DisplayName("Wconcept 크롤링 테스트")
    @Transactional
    void wconcept() throws IOException, URISyntaxException {
        // 할인 유무 상관 없음
        ItemRequest w = crawlingService.of("https://www.wconcept.co.kr/Product/303137901");

        System.out.println("brand: " + w.getBrand() + " name: " + w.getName());
        System.out.println("originalPrice: " + w.getOriginalPrice() + " discountRate: " + w.getDiscountRate() + " discountedPrice: " + w.getDiscountedPrice());
        System.out.println("imgUrl: " + w.getImageUrl());
        System.out.println("from: " + w.getItemProvider().getValue());

    }

    @Test
    @DisplayName("Ably 크롤링 테스트")
    @Transactional
    void ably() throws IOException, URISyntaxException {
        ItemRequest item = crawlingService.of("https://m.a-bly.com/goods/8167148");

        System.out.println("brand: " + item.getBrand() + " name: " + item.getName());
        System.out.println("originalPrice: " + item.getOriginalPrice() + " discountRate: " + item.getDiscountRate() + " discountedPrice: " + item.getDiscountedPrice());
        System.out.println("imgUrl: " + item.getImageUrl());
        System.out.println("from: " + item.getItemProvider().getValue());
    }

    @Test
    @DisplayName("Wconcept 부하 테스트")
    @Transactional
    void WconceptPlus() throws URISyntaxException, IOException {
        int TIME = 50;

        String uri = "https://www.wconcept.co.kr/Product/303137901";
        // Html 파싱 기반
        long beforeTime1 = System.currentTimeMillis();

        for (int i = 0; i < TIME; i++) {
            crawlingService.getWConcept(uri);
        }

        long afterTime1 = System.currentTimeMillis();
        long secDiffTime1 = afterTime1 - beforeTime1;

        System.out.println("HTML 파싱");
        System.out.println("시간차이(m) : " + secDiffTime1);

        System.out.println("----------------------------------");
        // Json 기반
        long beforeTime2 = System.currentTimeMillis();

        for (int i = 0; i < TIME; i++) {
            crawlingService.getWConceptJson(uri);
        }

        long afterTime2 = System.currentTimeMillis();
        long secDiffTime2 = afterTime2 - beforeTime2;
        System.out.println("Json 파싱");
        System.out.println("시간차이(m) : " + secDiffTime2);
        System.out.println("----------------------------------");
        System.out.println("HTML / Json %: " + secDiffTime1 / secDiffTime2 * 100);


    }
}