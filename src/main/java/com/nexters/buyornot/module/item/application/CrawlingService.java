package com.nexters.buyornot.module.item.application;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.item.domain.ItemProvider;
import com.nexters.buyornot.module.item.api.request.ItemRequest;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.nexters.buyornot.global.common.codes.ErrorCode.NOT_SUPPORTED_CRAWLING_EXCEPTION;

@Service
public class CrawlingService {
    private static final String MUSINSA = "musinsa";
    private static final String ZIGZAG = "zigzag";
    private static final String TWENTYNINCECM = "29cm";
    private static final String WCONCEPT = "wconcept";
    private static final String ABLY = "a-bly";
    private static final String CLIENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5.1 Safari/605.1.15";

    public ItemRequest of(String url) throws IOException {

        if (url.contains(MUSINSA)) return getMusinsa(url);
        if (url.contains(ZIGZAG)) return getZigzag(url);
        if (url.contains(TWENTYNINCECM)) return get29cm(url);
        if (url.contains(WCONCEPT)) return getWConcept(url);
        if (url.contains(ABLY)) return getAbly(url);
        else throw new BusinessExceptionHandler(NOT_SUPPORTED_CRAWLING_EXCEPTION);

//        return ItemRequest.defaultConfig();
    }

    public ItemRequest getMusinsa(String url) throws IOException {

        String brand, itemName, imgUrl, originPrice, discountRate;
        discountRate = "0";
        double discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", CLIENT)
                .get();

        //상품 이미지
        Elements imageBlock = document.getElementsByClass("product-img");

        imgUrl = "https:" + imageBlock.get(0).select("img").attr("src");

        //가격
        originPrice = document.getElementById("normal_price").text().replaceAll("[^0-9]", "");
        discountRate = document.getElementsByClass("txt_kor_discount").text().replaceAll("[^0-9]", "");

        if (!discountRate.isEmpty()) {
            discountedPrice = calculatePrice(originPrice, discountRate);
        } else {
            discountedPrice = Double.parseDouble(originPrice);
        }

        //브랜드, 상품명
        Elements infoBlock = document.getElementsByClass("product_title");
        itemName = infoBlock.select("em").html();
        Elements brandBlock = document.getElementsByClass("product_article_contents");
        brand = brandBlock.select("a").get(0).html();

        return ItemRequest.newItemDto(ItemProvider.MUSINSA, brand, itemName, url, imgUrl, originPrice, discountRate, discountedPrice);

    }

    public ItemRequest get29cm(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice, discountRate;
        double discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", CLIENT)
                .get();

        imgUrl = document.getElementsByClass("css-122y91a ewptmlp4").select("img").attr("src");
        brand = document.getElementsByClass("css-ehtr91 e1lehz0e2").text();
        itemName = document.select("title").html();

        discountRate = document.getElementsByClass("css-pnhbjr ent7twr2").text().replaceAll("[^0-9]", "");

        if (discountRate.isEmpty()) {
            originPrice = document.getElementsByClass("css-4bcxzt ent7twr4").text().replaceAll("[^0-9]", "");
            discountedPrice = Double.parseDouble(originPrice);
        } else {
            originPrice = document.getElementsByClass("css-1bci2fm ent7twr1").html();
            originPrice = originPrice.replaceAll("[^0-9]", "");

            String dp = document.getElementsByClass("css-4bcxzt ent7twr4").text();
            dp = dp.replaceAll("[^0-9]", "");

            // discountPrice 크롤링 구문
            if (!dp.isBlank()) {
                discountedPrice = calculatePrice(originPrice, dp);
            } else {
                discountedPrice = calculatePrice(originPrice, discountRate);
            }
        }

        return ItemRequest.newItemDto(ItemProvider.ZIGZAG, brand, itemName, url, imgUrl, originPrice, discountRate, discountedPrice);

    }

    public ItemRequest getZigzag(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice, discountRate;
        double discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", CLIENT)
                .get();

        Elements elements = document.getAllElements();
        brand = elements.select("meta[property=product:brand]").attr("content");
        itemName = elements.select("title").html();
        imgUrl = elements.select("link[rel=preload]").attr("href");

        JsonParser parser = new JsonParser();
        JsonObject priceBlock = (JsonObject) parser.parse(elements.select("script[id=__NEXT_DATA__]").html());
        JsonObject props = priceBlock.get("props").getAsJsonObject();
        JsonObject pageProps = props.get("pageProps").getAsJsonObject();
        JsonObject product = pageProps.getAsJsonObject("product");
        JsonObject product_price = product.getAsJsonObject("product_price");
        originPrice = product_price.get("original_price").toString();
        discountRate = product_price.get("discount_rate").toString();

        String dp = document.getElementsByClass(" css-15ex2ru e1v14k971").text();
        dp = dp.replaceAll("[^0-9]", "");

        // discountPrice 크롤링 구문
        if (!dp.isBlank()) {
            discountedPrice = calculatePrice(originPrice, dp);
        } else {
            discountedPrice = calculatePrice(originPrice, discountRate);
        }

        return ItemRequest.newItemDto(ItemProvider.APLUSB, brand, itemName, url, imgUrl, originPrice, discountRate, discountedPrice);
    }

    public ItemRequest getWConcept(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice, discountRate = "0", discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", CLIENT)
                .get();

        brand = document.getElementsByClass("brand").first().text();
        itemName = document.getElementsByClass("product cottonusa").text();

        originPrice = document.getElementsByClass("normal").select("em").text();
        originPrice = originPrice.replace(",", "");

        if (originPrice.isEmpty()) {
            originPrice = document.getElementsByClass("sale").select("em").text();
            originPrice = originPrice.replaceAll("[^0-9]", "");
            discountedPrice = originPrice;
        } else {
            discountRate = document.getElementsByClass("discount_percent").text();
            discountRate = discountRate.replaceAll("[^0-9]", "");
            discountedPrice = document.getElementsByClass("sale").select("em").text();
            discountedPrice = discountedPrice.replaceAll("[^0-9]", "");
        }

        imgUrl = "https:" + document.getElementsByClass("img_area").select("img").attr("src");

        return ItemRequest.newItemDto(ItemProvider.WCONCEPT, brand, itemName, url, imgUrl, originPrice, discountRate, Double.parseDouble(discountedPrice));
    }

    //가격 정보 추가 필요
    public ItemRequest getAbly(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice = "0", discountRate, discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", CLIENT)
                .get();

        brand = document.getElementsByClass("AblyText_text___0rpe AblyText_text--gray70__OFZAj AblyText_text--subtitle2__RGq0D AblyText_text--subtitle2__fixed__XhWOS").text();
        itemName = document.getElementsByClass("AblyText_text___0rpe AblyText_text--gray70__OFZAj AblyText_text--body1__1bctZ AblyText_text--body1__fixed__lcImN sc-143650ee-0 czMDZc").text();
        imgUrl = document.getElementsByClass("sc-faa7651c-1 dghJsf").select("picture").get(0).select("img").attr("src");
//        originPrice = document.getElementsByClass("AblyText_text___0rpe AblyText_text--gray70__OFZAj AblyText_text--button__Q0TmE AblyText_text--button__fixed__QFv9Y").html();
//        originPrice = originPrice.replace(",", "");
        discountRate = "0";
        discountedPrice = "0";

        return ItemRequest.newItemDto(ItemProvider.ABLY, brand, itemName, url, imgUrl, originPrice, discountRate, Double.parseDouble(discountedPrice));
    }

    private double calculatePrice(String originPrice, String discountRate) {
        double price = Double.parseDouble(originPrice);
        double rate = discountRate.isBlank() ? 0.0 : Double.parseDouble(discountRate);
        double discount = price * (rate / 100.0);
        return price - discount;
    }
}
