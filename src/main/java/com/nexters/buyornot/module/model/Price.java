package com.nexters.buyornot.module.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
public class Price {

    private static final BigDecimal PRICE_MIN_VALUE = BigDecimal.ZERO;

    @Column(name = "original_price")
    private BigDecimal value;

    private int discountRate = 0;

    private BigDecimal discountedPrice;

    public Price(BigDecimal originalPrice, String discountRate, BigDecimal discountedPrice) {
        this.value = originalPrice;
        this.discountRate = Integer.parseInt(discountRate);
        this.discountedPrice = discountedPrice;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }
}
