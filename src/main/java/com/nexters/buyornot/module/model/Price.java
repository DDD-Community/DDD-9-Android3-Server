package com.nexters.buyornot.module.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Price {

    private static final BigDecimal PRICE_MIN_VALUE = BigDecimal.ZERO;

    @Column(name = "price")
    private BigDecimal value;

    private int discountRate = 0;
}
