package com.tenseconds.ev.model;

import java.math.BigDecimal;

public enum ChargerType {
    AC_7KW(BigDecimal.valueOf(7.0), BigDecimal.valueOf(1.00)),
    DC_30KW(BigDecimal.valueOf(30.0), BigDecimal.valueOf(1.15)),
    DC_60KW(BigDecimal.valueOf(60.0), BigDecimal.valueOf(1.30));

    private final BigDecimal ratedPowerKw;
    private final BigDecimal rateMultiplier;

    ChargerType(BigDecimal ratedPowerKw, BigDecimal rateMultiplier) {
        this.ratedPowerKw = ratedPowerKw;
        this.rateMultiplier = rateMultiplier;
    }

    public BigDecimal getRatedPowerKw() {
        return ratedPowerKw;
    }

    public BigDecimal getRateMultiplier() {
        return rateMultiplier;
    }
}
