package com.tenseconds.ev.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Session {
    private final String sessionId;
    private final String driverId;
    private final String chargerId;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalEnergyKwh;
    private BigDecimal runningCost;
    private int lastSeqNumber;
    private SessionState state;

    public Session(String sessionId, String driverId, String chargerId, LocalDateTime startTime) {
        this.sessionId = sessionId;
        this.driverId = driverId;
        this.chargerId = chargerId;
        this.startTime = startTime;
        this.totalEnergyKwh = BigDecimal.ZERO;
        this.runningCost = BigDecimal.ZERO;
        this.lastSeqNumber = 0;
        this.state = SessionState.ACTIVE;
    }

    public String getSessionId() { return sessionId; }
    public String getDriverId() { return driverId; }
    public String getChargerId() { return chargerId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public BigDecimal getTotalEnergyKwh() { return totalEnergyKwh; }
    public BigDecimal getRunningCost() { return runningCost; }
    public int getLastSeqNumber() { return lastSeqNumber; }
    public SessionState getState() { return state; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setTotalEnergyKwh(BigDecimal totalEnergyKwh) { this.totalEnergyKwh = totalEnergyKwh; }
    public void setRunningCost(BigDecimal runningCost) { this.runningCost = runningCost; }
    public void setLastSeqNumber(int lastSeqNumber) { this.lastSeqNumber = lastSeqNumber; }
    public void setState(SessionState state) { this.state = state; }
}
