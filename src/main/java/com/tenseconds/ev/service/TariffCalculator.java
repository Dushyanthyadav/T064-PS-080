package com.tenseconds.ev.service;

import com.tenseconds.ev.model.ChargerType;
import com.tenseconds.ev.model.MembershipTier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

public class TariffCalculator {
    private static final LocalTime OFF_PEAK_START = LocalTime.of(22, 0);
    private static final LocalTime NORMAL_START = LocalTime.of(6, 0);
    private static final LocalTime PEAK_START = LocalTime.of(18, 0);

    private static final BigDecimal OFF_PEAK_RATE = BigDecimal.valueOf(12.00);
    private static final BigDecimal NORMAL_RATE = BigDecimal.valueOf(16.00);
    private static final BigDecimal PEAK_RATE = BigDecimal.valueOf(22.00);
    private static final BigDecimal GST_RATE = BigDecimal.valueOf(0.18);

    public static BigDecimal getBaseRateForTime(LocalTime time) {
        if (!time.isBefore(NORMAL_START) && time.isBefore(PEAK_START)) {
            return NORMAL_RATE;
        } else if (!time.isBefore(PEAK_START) && time.isBefore(OFF_PEAK_START)) {
            return PEAK_RATE;
        } else {
            return OFF_PEAK_RATE;
        }
    }

    public static BigDecimal calculateTickCost(BigDecimal deltaKwh, LocalTime timestamp, 
                                               ChargerType chargerType, MembershipTier tier) {
        BigDecimal baseRate = getBaseRateForTime(timestamp);
        BigDecimal effectiveRate = baseRate.multiply(chargerType.getRateMultiplier());
        BigDecimal grossEnergyCost = deltaKwh.multiply(effectiveRate);

        BigDecimal discount = grossEnergyCost.multiply(tier.getDiscountRate());
        return grossEnergyCost.subtract(discount).setScale(4, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateInvoiceTotalWithGst(BigDecimal grossServicesTotal) {
        BigDecimal gst = grossServicesTotal.multiply(GST_RATE);
        BigDecimal grandTotal = grossServicesTotal.add(gst);
        return grandTotal.setScale(0, RoundingMode.HALF_UP);
    }
}
