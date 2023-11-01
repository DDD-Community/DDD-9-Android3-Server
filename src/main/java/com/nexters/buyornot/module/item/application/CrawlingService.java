package com.nexters.buyornot.module.item.application;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.item.api.request.ItemRequest;
import com.nexters.buyornot.module.item.domain.ItemProvider;
import com.nexters.buyornot.module.item.dto.ably.AblyInfoDto;
import com.nexters.buyornot.module.item.dto.twentynine.TwentyNineInfoDto;
import com.nexters.buyornot.module.item.dto.wconcept.WconceptInfoDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import static com.nexters.buyornot.global.common.codes.ErrorCode.NOT_SUPPORTED_CRAWLING_EXCEPTION;

@Service
public class CrawlingService {
    private static final String MUSINSA = "musinsa";
    private static final String ZIGZAG = "zigzag";
    private static final String TWENTYNINCECM = "29cm";
    private static final String WCONCEPT = "wconcept";
    private static final String ABLY = "a-bly";
    private static final String CLIENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5.1 Safari/605.1.15";
    @Value("${item.crawling.ably.token}")
    private String AblyXAnonymousToken;

    public ItemRequest of(String url) throws IOException, URISyntaxException {
        if (url.contains(MUSINSA)) return getMusinsa(url);
        if (url.contains(ZIGZAG)) return getZigzag(url);
        if (url.contains(TWENTYNINCECM)) return get29cmJson(url);
        if (url.contains(WCONCEPT)) return getWConceptJson(url);
        if (url.contains(ABLY)) return getAblyJson(url);
        else throw new BusinessExceptionHandler(NOT_SUPPORTED_CRAWLING_EXCEPTION);
    }

    public ItemRequest getMusinsa(String url) throws IOException {
        String brand, itemName, imgUrl, originPrice, discountRate;
        double discountedPrice;

        Document document = Jsoup.connect(url)
                .header("userAgent", CLIENT)
                .get();

        //상품 이미지
        imgUrl = "https:" + Objects.requireNonNull(document.getElementById("bigimg")).attr("src");

        //가격
        originPrice = document.getElementById("normal_price").text().replaceAll("[^0-9]", "");
        discountRate = document.getElementsByClass("txt_kor_discount").text().replaceAll("[^0-9]", "");
        discountRate = discountRate.isEmpty() ? "0" : discountRate;
        discountedPrice = calculatePrice(originPrice, discountRate);

        //브랜드, 상품명
        Elements infoBlock = document.getElementsByClass("product_title");
        itemName = infoBlock.select("em").html();
        Elements brandBlock = document.getElementsByClass("product_article_contents");
        brand = brandBlock.select("a").get(0).html();

        return ItemRequest.newItemDto(ItemProvider.MUSINSA, brand, itemName, url, imgUrl, originPrice, discountRate, discountedPrice);

    }

    public ItemRequest get29cmJson(String url) throws IOException, URISyntaxException {
        String productId = getProductId(url);
        String uri = String.format("https://search-api.29cm.co.kr/api/v4/products/%s", productId);

        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.getForEntity(uri, TwentyNineInfoDto.class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new BusinessExceptionHandler(NOT_SUPPORTED_CRAWLING_EXCEPTION);
        }

        var result = response.getBody().getData();
        var itemNo = result.getItemNo();
        var itemName = result.getItemName();
        var subItemName = result.getSubjectDescriptions().isEmpty() ? null : result.getSubjectDescriptions().get(0);
        if (subItemName != null) {
            itemName += "_" + subItemName;
        }
        var brandNameKor = result.getFrontBrandNameKor();
        var imageUrl = "https://img.29cm.co.kr" + result.getImageUrl();
        var consumerPrice = String.valueOf(result.getSaleInfoV2().getConsumerPrice());
        var saleRate = String.valueOf(result.getSaleInfoV2().getSaleRate());
        var sellPrice = result.getSaleInfoV2().getSellPrice();

        return ItemRequest.newItemDto(ItemProvider.APLUSB, brandNameKor, itemName,
                url, imageUrl, consumerPrice, saleRate, sellPrice);
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

    public ItemRequest getWConceptJson(String url) throws URISyntaxException {
        String productId = getProductId(url);
        String uri = "https://www.wconcept.co.kr/Ajax/GetProductsInfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("itemcds", productId);

        var httpEntity = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, WconceptInfoDto[].class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new BusinessExceptionHandler(NOT_SUPPORTED_CRAWLING_EXCEPTION);
        }
        var result = Arrays.stream(Objects.requireNonNull(response.getBody())).findFirst().orElseThrow(() ->
                new BusinessExceptionHandler(NOT_SUPPORTED_CRAWLING_EXCEPTION)
        );

        var itemCd = result.getItemCd();
        var itemName = result.getItemName();
        var brandNameKr = result.getBrandNameKr();
        var imageUrlDesktop = "https:" + result.getImageUrlDesktop();
        var customerPrice = String.valueOf(result.getCustomerPrice());
        var finalDiscountRate = String.valueOf(result.getFinalDiscountRate().intValue());
        var finalPrice = result.getFinalPrice();

        return ItemRequest.newItemDto(ItemProvider.WCONCEPT, brandNameKr, itemName,
                url, imageUrlDesktop, customerPrice, finalDiscountRate, finalPrice);
    }

    public ItemRequest getAblyJson(String url) throws URISyntaxException {
        String productId = getProductId(url);
        String uri = String.format("https://api.a-bly.com/api/v2/goods/%s", productId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Anonymous-Token", AblyXAnonymousToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AblyInfoDto> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, AblyInfoDto.class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new BusinessExceptionHandler(NOT_SUPPORTED_CRAWLING_EXCEPTION);
        }
        var goods = Objects.requireNonNull(response.getBody()).getGoods();

        var sno = goods.getSno();
        var image = goods.getImage();
        var name = goods.getName();
        var storeName = goods.getMarket().getName();
        var originalPrice = String.valueOf(goods.getLinkedOption().getOriginalPrice());
        var discountRate = String.valueOf(goods.getDiscountRate());
        var price = goods.getPrice();

        return ItemRequest.newItemDto(ItemProvider.ABLY, storeName, name, url,
                image, originalPrice, discountRate, price);
    }

    private static String getProductId(String url) throws URISyntaxException {
        String productId = null;
        URI parsedUri = new URI(url);
        String[] pathSegments = parsedUri.getPath().split("/");
        if (pathSegments.length > 1) {
            productId = pathSegments[pathSegments.length - 1];
        }
        return productId;
    }

    private double calculatePrice(String originPrice, String discountRate) {
        double price = Double.parseDouble(originPrice);
        double rate = discountRate.isBlank() ? 0.0 : Double.parseDouble(discountRate);
        double discount = price * (rate / 100.0);
        return price - discount;
    }

    @Deprecated
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

    @Deprecated
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
}
