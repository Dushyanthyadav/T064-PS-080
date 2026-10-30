package com.tenseconds.ev;

import com.tenseconds.ev.model.*;
import com.tenseconds.ev.service.ChargingEngine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("  10 SECONDS Capstone - PS-080 EV Charging Engine (T064) ");
        System.out.println("=========================================================");

        ChargingEngine engine = new ChargingEngine();

        Driver driver = new Driver("D001", "Dushyanth Yadav", BigDecimal.valueOf(500.00), MembershipTier.GOLD);
        Charger charger = new Charger("C001", "SITE-A", ChargerType.DC_30KW);

        engine.registerDriver(driver);
        engine.registerCharger(charger);

        LocalDateTime now = LocalDateTime.now();
        Session session = engine.startSession("S001", "D001", "C001", now);
        System.out.println("Session started: " + session.getSessionId() + " on " + charger.getChargerId());

        for (int i = 1; i <= 10; i++) {
            engine.processMeterTick("S001", i, BigDecimal.valueOf(0.01), now.plusMinutes(i));
        }

        Session completed = engine.stopSession("S001", now.plusMinutes(15), SessionState.COMPLETED);
        System.out.println("Session completed: State=" + completed.getState());
        System.out.println("Total Energy: " + completed.getTotalEnergyKwh() + " kWh");
        System.out.println("Driver Remaining Balance: Rs " + driver.getWalletBalance());
    }
}
