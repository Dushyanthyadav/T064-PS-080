package com.tenseconds.ev;

import com.tenseconds.ev.model.*;
import com.tenseconds.ev.service.ChargingEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ChargingEngineTest {
    private ChargingEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ChargingEngine();
        Driver d1 = new Driver("D-01", "Test Driver", BigDecimal.valueOf(250.00), MembershipTier.SILVER);
        Charger c1 = new Charger("C-01", "SITE-01", ChargerType.DC_30KW);

        engine.registerDriver(d1);
        engine.registerCharger(c1);
    }

    @Test
    @DisplayName("BR-08: Block session when wallet is under Rs 200")
    void testStartSessionInsufficientBalance() {
        Driver poorDriver = new Driver("D-02", "Low Balance", BigDecimal.valueOf(150.00), MembershipTier.SILVER);
        engine.registerDriver(poorDriver);

        assertThrows(IllegalStateException.class, () -> 
            engine.startSession("S-02", "D-02", "C-01", LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("Core Flow: Start, Process Ticks, and Stop Session successfully")
    void testHappyPathSessionExecution() {
        LocalDateTime start = LocalDateTime.of(2026, 10, 30, 10, 0);
        Session session = engine.startSession("S-01", "D-01", "C-01", start);

        assertNotNull(session);
        assertEquals(SessionState.ACTIVE, session.getState());
        assertTrue(engine.getCharger("C-01").isOccupied());

        // Process multiple ticks so total energy bill rounds to >= 1 rupee
        for (int i = 1; i <= 5; i++) {
            boolean tickSuccess = engine.processMeterTick("S-01", i, BigDecimal.valueOf(0.10), start.plusSeconds(30 * i));
            assertTrue(tickSuccess);
        }

        Session stopped = engine.stopSession("S-01", start.plusMinutes(10), SessionState.COMPLETED);
        assertEquals(SessionState.COMPLETED, stopped.getState());
        assertFalse(engine.getCharger("C-01").isOccupied());
        assertTrue(engine.getDriver("D-01").getWalletBalance().compareTo(BigDecimal.valueOf(250.00)) < 0);
    }

    @Test
    @DisplayName("BR-08: Auto-stop triggers when balance margin drops below Rs 20")
    void testAutoStopOnLowBalanceMargin() {
        Driver borderlineDriver = new Driver("D-03", "Edge Driver", BigDecimal.valueOf(200.00), MembershipTier.SILVER);
        Charger fastCharger = new Charger("C-02", "SITE-01", ChargerType.DC_60KW);

        engine.registerDriver(borderlineDriver);
        engine.registerCharger(fastCharger);

        LocalDateTime time = LocalDateTime.of(2026, 10, 30, 19, 0);
        engine.startSession("S-03", "D-03", "C-02", time);

        boolean running = engine.processMeterTick("S-03", 1, BigDecimal.valueOf(8.0), time.plusMinutes(5));

        assertFalse(running);
        assertEquals(SessionState.AUTO_STOPPED_LOW_BALANCE, engine.getSession("S-03").getState());
        assertFalse(engine.getCharger("C-02").isOccupied());
    }
}
