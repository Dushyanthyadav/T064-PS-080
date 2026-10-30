package com.tenseconds.ev.model;

import java.math.BigDecimal;

public class Driver {
    private final String driverId;
    private final String name;
    private BigDecimal walletBalance;
    private final MembershipTier tier;

    public Driver(String driverId, String name, BigDecimal initialWallet, MembershipTier tier) {
        this.driverId = driverId;
        this.name = name;
        this.walletBalance = initialWallet;
        this.tier = tier;
    }

    public String getDriverId() { return driverId; }
    public String getName() { return name; }
    public BigDecimal getWalletBalance() { return walletBalance; }
    public MembershipTier getTier() { return tier; }

    public synchronized void debit(BigDecimal amount) {
        this.walletBalance = this.walletBalance.subtract(amount);
    }
}
