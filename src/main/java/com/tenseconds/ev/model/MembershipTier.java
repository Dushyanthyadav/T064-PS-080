package com.tenseconds.ev.model;

import java.math.BigDecimal;

public enum MembershipTier {
    SILVER(BigDecimal.ZERO),
    GOLD(BigDecimal.valueOf(0.10)),
    PLATINUM(BigDecimal.valueOf(0.15));

    private final BigDecimal discountRate;

    MembershipTier(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }
}
