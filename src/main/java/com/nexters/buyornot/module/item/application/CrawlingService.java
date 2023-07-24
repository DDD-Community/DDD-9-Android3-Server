package com.nexters.buyornot.module.item.application;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nexters.buyornot.module.item.domain.ItemProvider;
import com.nexters.buyornot.module.item.dto.ItemDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CrawlingService {

    public ItemDto of(String url) throws IOException {

        if(url.contains("musinsa")) return getMusinsa(url);
        if(url.contains("zigzag")) return getZigzag(url);
        if(url.contains("29cm")) return get29cm(url);
        if(url.contains("wconcept")) return getWConcept(url);

        //w컨셉, 에이블리

        return ItemDto.defaultConfig();
    }

    public ItemDto getMusinsa(String url) throws IOException {

        String brand, itemName, imgUrl, originPrice, discountRate;
        double discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5.1 Safari/605.1.15")
                .get();

        //상품 이미지
        Elements imageBlock = document.getElementsByClass("product-img");

        imgUrl = "https:" + imageBlock.get(0).select("img").attr("src");

        //가격
        originPrice = document.getElementById("normal_price").text();
        discountRate = document.getElementsByClass("txt_kor_discount").text();

        if(!discountRate.isEmpty()) {
            int discountIdx = discountRate.indexOf("%");
            discountRate = discountRate.substring(0, discountIdx);
            discountedPrice = calculatePrice(originPrice, discountRate);
        } else {
            discountRate = "0";
            discountedPrice = Double.parseDouble(originPrice);
        }

        //브랜드, 상품명
        Elements infoBlock = document.getElementsByClass("product_title");
        itemName = infoBlock.select("em").html();
        Elements brandBlock = document.getElementsByClass("product_article_contents");
        brand = brandBlock.select("a").get(0).html();

        return ItemDto.newItemDto(ItemProvider.MUSINSA, brand, itemName, url, imgUrl, originPrice, discountRate, discountedPrice);

    }

    public ItemDto get29cm(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice, discountRate, discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5.1 Safari/605.1.15")
                .get();

        imgUrl = document.getElementsByClass("css-122y91a ewptmlp4").select("img").attr("src");
        brand = document.getElementsByClass("css-ehtr91 e1lehz0e2").text();
        itemName = document.select("title").html();

        discountRate = document.getElementsByClass("css-pnhbjr ent7twr2").text();
        discountRate = discountRate.replace("%", "");

        if(discountRate.isEmpty()) {
            discountRate = "0";
            originPrice = document.getElementsByClass("css-4bcxzt ent7twr4").text();
            originPrice = originPrice.replace(",", "");
            originPrice = originPrice.replace("원", "");
            discountedPrice = originPrice;
        } else {
            originPrice = document.getElementsByClass("css-1bci2fm ent7twr1").html();
            originPrice = originPrice.replace(",", "");
            discountedPrice = document.getElementsByClass("css-4bcxzt ent7twr4").text();
            discountedPrice = discountedPrice.replace(",", "");
            discountedPrice = discountedPrice.replace("원", "");
        }

        return ItemDto.newItemDto(ItemProvider.ZIGZAG, brand, itemName, url, imgUrl, originPrice, discountRate, Double.parseDouble(discountedPrice));

    }

    public ItemDto getZigzag(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice, discountRate, discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5.1 Safari/605.1.15")
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

        discountedPrice = document.getElementsByClass(" css-15ex2ru e1v14k971").text();
        discountedPrice = discountedPrice.replace(",", "");
        discountedPrice = discountedPrice.replace("원", "");

        return ItemDto.newItemDto(ItemProvider.APLUSB, brand, itemName, url, imgUrl, originPrice, discountRate, Double.parseDouble(discountedPrice));
    }

    public ItemDto getWConcept(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice, discountRate, discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5.1 Safari/605.1.15")
                .get();

        brand = document.getElementsByClass("brand").first().text();
        itemName = document.getElementsByClass("product cottonusa").text();

        originPrice = document.getElementsByClass("normal").select("em").text();
        originPrice = originPrice.replace(",", "");

        if(originPrice.isEmpty()) {
            discountRate = "0";
            originPrice = document.getElementsByClass("sale").select("em").text();
            originPrice = originPrice.replace(",", "");
            discountedPrice = originPrice;
        } else {
            discountRate = document.getElementsByClass("discount_percent").text();
            discountRate = discountRate.replace("%", "");
            discountedPrice = document.getElementsByClass("sale").select("em").text();
            discountedPrice = discountedPrice.replace(",", "");
        }

        imgUrl = "https:" + document.getElementsByClass("img_area").select("img").attr("src");

        return ItemDto.newItemDto(ItemProvider.WCONCEPT, brand, itemName, url, imgUrl, originPrice, discountRate, Double.parseDouble(discountedPrice));
    }

    private double calculatePrice(String originPrice, String discountRate) {
        double price = Double.parseDouble(originPrice);
        double discount = price * (Double.parseDouble(discountRate) / 100.0);
        return price - discount;
    }
}
