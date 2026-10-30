package com.tenseconds.ev.model;

public class Charger {
    private final String chargerId;
    private final String siteId;
    private final ChargerType type;
    private boolean occupied;

    public Charger(String chargerId, String siteId, ChargerType type) {
        this.chargerId = chargerId;
        this.siteId = siteId;
        this.type = type;
        this.occupied = false;
    }

    public String getChargerId() { return chargerId; }
    public String getSiteId() { return siteId; }
    public ChargerType getType() { return type; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
}
