package com.tenseconds.ev.service;

import com.tenseconds.ev.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ChargingEngine {
    private final Map<String, Driver> drivers = new HashMap<>();
    private final Map<String, Charger> chargers = new HashMap<>();
    private final Map<String, Session> sessions = new HashMap<>();

    public static final BigDecimal MIN_START_BALANCE = BigDecimal.valueOf(200.00);
    public static final BigDecimal CUTOFF_SAFETY_MARGIN = BigDecimal.valueOf(20.00);

    public void registerDriver(Driver driver) {
        drivers.put(driver.getDriverId(), driver);
    }

    public void registerCharger(Charger charger) {
        chargers.put(charger.getChargerId(), charger);
    }

    public Session startSession(String sessionId, String driverId, String chargerId, LocalDateTime startTime) {
        Driver driver = drivers.get(driverId);
        Charger charger = chargers.get(chargerId);

        if (driver == null || charger == null) {
            throw new IllegalArgumentException("Driver or Charger does not exist.");
        }
        if (charger.isOccupied()) {
            throw new IllegalStateException("Charger is already in use.");
        }
        if (driver.getWalletBalance().compareTo(MIN_START_BALANCE) < 0) {
            throw new IllegalStateException("Insufficient balance to start session. Min required: Rs " + MIN_START_BALANCE);
        }

        Session session = new Session(sessionId, driverId, chargerId, startTime);
        charger.setOccupied(true);
        sessions.put(sessionId, session);
        return session;
    }

    public boolean processMeterTick(String sessionId, int seqNumber, BigDecimal deltaKwh, LocalDateTime timestamp) {
        Session session = sessions.get(sessionId);
        if (session == null || session.getState() != SessionState.ACTIVE) {
            return false;
        }

        if (seqNumber != session.getLastSeqNumber() + 1) {
            session.setState(SessionState.DISPUTED);
        }
        session.setLastSeqNumber(seqNumber);

        Driver driver = drivers.get(session.getDriverId());
        Charger charger = chargers.get(session.getChargerId());

        BigDecimal tickCost = TariffCalculator.calculateTickCost(deltaKwh, timestamp.toLocalTime(), 
                                                               charger.getType(), driver.getTier());

        session.setTotalEnergyKwh(session.getTotalEnergyKwh().add(deltaKwh));
        session.setRunningCost(session.getRunningCost().add(tickCost));

        BigDecimal remainingBuffer = driver.getWalletBalance().subtract(session.getRunningCost());
        if (remainingBuffer.compareTo(CUTOFF_SAFETY_MARGIN) <= 0) {
            stopSession(sessionId, timestamp, SessionState.AUTO_STOPPED_LOW_BALANCE);
            return false;
        }

        return true;
    }

    public Session stopSession(String sessionId, LocalDateTime endTime, SessionState targetState) {
        Session session = sessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found.");
        }

        session.setEndTime(endTime);
        session.setState(targetState);

        Charger charger = chargers.get(session.getChargerId());
        if (charger != null) {
            charger.setOccupied(false);
        }

        Driver driver = drivers.get(session.getDriverId());
        if (driver != null) {
            BigDecimal finalCharge = TariffCalculator.calculateInvoiceTotalWithGst(session.getRunningCost());
            driver.debit(finalCharge);
        }

        return session;
    }

    public Session getSession(String sessionId) { return sessions.get(sessionId); }
    public Driver getDriver(String driverId) { return drivers.get(driverId); }
    public Charger getCharger(String chargerId) { return chargers.get(chargerId); }
}
