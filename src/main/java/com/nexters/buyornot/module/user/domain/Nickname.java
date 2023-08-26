package com.nexters.buyornot.module.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Nickname {
    RING("반지"),
    SHOULDER_BAG("숄더백"),
    BACKPACK("백팩"),
    CAP("모자"),
    GLASSES("안경"),
    TOTE_BAG("토트백"),
    BEANIE("비니"),
    WRISTWATCH("시계"),
    WALLET("지갑"),
    SUNGLASSES("선글라스"),
    MIDDLE_HEELS("미들힐"),
    LOAFER("로퍼"),
    HIGH_HEELS("하이힐"),
    COMBAT_BOOTS("워커"),
    PEARL_NECKLACE("진주목걸이"),
    ONE_PIECE("원피스"),
    SLEEVELESS("민소매"),
    JEANS("청바지"),
    BRACELET("팔찌"),
    DIAMOND_NECKLACE("다이아목걸이");

    String value;
}
